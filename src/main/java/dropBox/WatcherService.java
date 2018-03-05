package dropBox;

import database.DataUpdate;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static java.lang.String.format;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

@SuppressWarnings("unchecked")
public class WatcherService {

    private DropBoxService dropBoxService;
    private String fileName;
    private DataUpdate dataUpdate;

    public WatcherService(DataUpdate dataUpdate, DropBoxService dropBoxService) {
        this.dropBoxService = dropBoxService;
        this.dataUpdate = dataUpdate;
    }

    public void watch(String source) throws IOException {
        WatchService watcher = preparingObserver(source);
        while (true) {
            try {
                WatchKey key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    fileName = ev.context().getFileName().toString();
                    dataUpdate.addOrUpdate(fileName, new SenderWorker(toFile(source, ev), fileName, dropBoxService));
                }
                if (!key.reset()) throw new Exception();
            } catch (Exception ex) {
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