import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class WatcherService {

    public void watch(String source) throws IOException {

        WatchService watcher = FileSystems.getDefault().newWatchService();
        Path dir = Paths.get(source);
        dir.register(watcher, ENTRY_CREATE);
        System.out.println("Watch Service registered for dir: " + dir.getFileName());
        while (true) {
            try {
                WatchKey key;
                key = watcher.take();
                @SuppressWarnings("unchecked")
                WatchEvent<Path> ev = (WatchEvent<Path>) key.pollEvents().get(0);
                Path fileName = Paths.get(source + "/" + ev.context().getFileName());
                System.out.println("NOWY PLIK: " + fileName.getFileName());

                boolean valid = key.reset();
                if (!valid) {
                    throw new Exception();
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
    }
}
