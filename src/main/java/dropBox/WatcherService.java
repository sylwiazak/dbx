package dropBox;

import database.FileTransferHistory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

@SuppressWarnings("unchecked")
public class WatcherService {

    private DropBoxService dropBoxService;
    private Session session;
    private long start;
    private long finish;
    private FileTransferHistory fileTransferHistory;

    public WatcherService(DropBoxService dropBoxService, Session session) {
        this.dropBoxService = dropBoxService;
        this.session = session;

    }

    public void watch(String source) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        WatchService watcher = FileSystems.getDefault().newWatchService();
        Path dir = Paths.get(source);
        dir.register(watcher, ENTRY_CREATE);
        while (true) {
            try {
                Transaction transaction = session.beginTransaction();
                WatchKey key;
                key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    start = currentTimeMillis();
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    System.out.println(ev.context().getFileName());
                    List<FileTransferHistory> fileList = session.createQuery("from FileTransferHistory").getResultList();
                    List<String> listOfFileNames = fileList.stream().map(FileTransferHistory::getName).collect(Collectors.toList());//stream z nazwami plików, później w ifie zrobić equals
                    if (!listOfFileNames.contains(ev.context().getFileName().toString())) {
                        executorService.submit(new SenderWorker(toFile(source, ev), ev.context().getFileName().toString(), dropBoxService));
                        finish = currentTimeMillis();
                        fileTransferHistory = new FileTransferHistory();
                        fileTransferHistory.setName(ev.context().getFileName().toString());
                        fileTransferHistory.setSendingDate(LocalDate.now());
                        fileTransferHistory.setSendingTime(finish - start);
                        session.save(fileTransferHistory);
                    }
                }
                session.clear();
                if (transaction.isActive())
                    session.getTransaction().commit();
                if (!key.reset()) throw new Exception();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private File toFile(String source, WatchEvent<Path> ev) {
        return new File(format("%s/%s", source, ev.context().getFileName()));
    }
}
