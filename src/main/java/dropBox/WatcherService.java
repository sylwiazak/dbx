package dropBox;

import database.FileTransferHistoryDao;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

@SuppressWarnings("unchecked")
public class WatcherService {

    private DropBoxService dropBoxService;
    private FileTransferHistoryDao fileTransferHistoryDao;
    private long start;
    private long finish;
    private String fileName;
    private ExecutorService executorService = Executors.newFixedThreadPool(4);


    public WatcherService(DropBoxService dropBoxService, FileTransferHistoryDao fileTransferHistoryDao) {
        this.dropBoxService = dropBoxService;
        this.fileTransferHistoryDao = fileTransferHistoryDao;

    }

    public void watch(String source) throws IOException {
        WatchService watcher = preparingObserver(source);
        while (true) {
            try {
                WatchKey key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    start = currentTimeMillis();
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    fileName = ev.context().getFileName().toString();
                    List<String> listOfFileNamesInDatabase = fileTransferHistoryDao.getListFile();

                    if (!listOfFileNamesInDatabase.contains(fileName)) {
                        executorService.submit(new SenderWorker(toFile(source, ev), fileName, dropBoxService));
                        finish = currentTimeMillis();
                        fileTransferHistoryDao.addFile(fileName, finish - start);
                    }
                    else {
                        finish = currentTimeMillis();
                        fileTransferHistoryDao.updateFile(fileName, finish - start);
                    }
                }
                if (!key.reset()) throw new Exception();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private WatchService preparingObserver(String source) throws IOException {
        WatchService watcher = FileSystems.getDefault().newWatchService();
        Path dir = Paths.get(source);
        dir.register(watcher, ENTRY_CREATE);
        return watcher;
    }

    private File toFile(String source, WatchEvent<Path> ev) {
        return new File(format("%s/%s", source, ev.context().getFileName()));
    }
}
