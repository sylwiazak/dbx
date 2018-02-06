import com.dropbox.core.DbxException;

import java.io.File;
import java.io.IOException;

public class RunnableClass implements Runnable {
    private DropBoxService dropBoxService;
    private File file;
    private String targetPath;

    public RunnableClass(File file, String targetPath) {
       this.file=file;
       this.targetPath=targetPath;
    }

    @Override
    public void run() {
        try {
            dropBoxService.send(file,targetPath);
        } catch (IOException | DbxException e) {
            e.printStackTrace();
        }
    }
}
