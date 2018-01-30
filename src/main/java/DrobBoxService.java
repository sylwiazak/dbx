import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v1.DbxClientV1;
import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v1.DbxWriteMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

public class DrobBoxService {

    private DbxClientV1 clientV1;

    public DrobBoxService(String accessToken) {
        DbxRequestConfig config = new DbxRequestConfig("First Dbx", Locale.getDefault().toString());
        clientV1= new DbxClientV1(config,accessToken);
    }

    public void send(File file, String targetPath) throws IOException, DbxException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            DbxEntry.File uploadedFile = clientV1.uploadFile(targetPath, DbxWriteMode.add(), file.length(), inputStream);
            System.out.println("Uploaded: " + uploadedFile.toString());
        }
    }
}
