package dropBox;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DropBoxService {

    private DbxClientV2 client;

    public DropBoxService(String accessToken) {
        DbxRequestConfig config = new DbxRequestConfig("First Dbx");
        client = new DbxClientV2(config, accessToken);
    }

    public void send(File file, String targetPath) throws IOException, DbxException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            client.files().uploadBuilder("/" + targetPath).uploadAndFinish(inputStream);
        }
    }
}
