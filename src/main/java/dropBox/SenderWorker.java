package dropBox;

import com.dropbox.core.DbxException;

import java.io.File;
import java.io.IOException;

public class SenderWorker implements Runnable {
    private final File file;
    private final String targetPath;
    private final DropBoxService dropBoxService;

    public SenderWorker(File file, String targetPath, DropBoxService dropBoxService) {
        this.file = file;
        this.targetPath = targetPath;
        this.dropBoxService = dropBoxService;
    }

    @Override
    public void run() {
        try {
            dropBoxService.send(file, targetPath);
        } catch (IOException | DbxException e) {
            e.printStackTrace();
        }
    }
}
