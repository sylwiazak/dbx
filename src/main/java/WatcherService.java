import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class WatcherService {

    public void watch(String source) throws IOException {

        WatchService watcher = FileSystems.getDefault().newWatchService();
        Path dir = Paths.get(source);
        dir.register(watcher, ENTRY_CREATE);
        while (true) {
            try {
                WatchKey key;
                key = watcher.take();
                @SuppressWarnings("unchecked")
                WatchEvent<Path> ev = (WatchEvent<Path>) key.pollEvents().get(0);
                Path fileName = Paths.get(source + "/" + ev.context().getFileName());
                new DropBoxService("accessToken")
                        .send(new File(source + "/" + ev.context().getFileName()), "/"+fileName.getFileName());

                if (!key.reset()) throw new Exception();

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
    }
}
