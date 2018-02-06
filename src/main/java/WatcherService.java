import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class WatcherService {
    private DropBoxService dropBoxService;

    public WatcherService(DropBoxService dropBoxService) {
        this.dropBoxService = dropBoxService;
    }

    public void watch(String source) throws IOException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        WatchService watcher = FileSystems.getDefault().newWatchService();
        Path dir = Paths.get(source);
        dir.register(watcher, ENTRY_CREATE);
        while (true) {
            try {
                WatchKey key;
                key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    executorService.submit(new RunnableClass(new File(source + "/" + ev.context().getFileName()), "/" + ev.context().getFileName())
                    );
                }
                if (!key.reset()) throw new Exception();
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
    }
}
