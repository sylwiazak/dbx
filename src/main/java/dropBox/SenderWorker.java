package dropBox;

import com.dropbox.core.DbxException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

public class SenderWorker implements Callable<Long> {
    private final File file;
    private final String targetPath;
    private final DropBoxService dropBoxService;

    public SenderWorker(File file, String targetPath, DropBoxService dropBoxService) {
        this.file = file;
        this.targetPath = targetPath;
        this.dropBoxService = dropBoxService;
    }

    @Override
    public Long call() throws Exception {
        try {
            Thread.sleep(100);
            Long start = System.currentTimeMillis();
            dropBoxService.send(file, targetPath);
            Long finish = System.currentTimeMillis();
            return finish - start;
        } catch (IOException | DbxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
