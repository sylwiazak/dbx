import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v1.DbxClientV1;
import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v1.DbxWriteMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DrobBoxService {

    private DbxClientV1 clientV1;

    public DrobBoxService(DbxRequestConfig config, String accessToken) {
        new DbxClientV1(config,accessToken);
    }

    public void send(File file, String targetPath) throws IOException, DbxException {
        FileInputStream inputStream= new FileInputStream(file);
        try {
            DbxEntry.File uploadedFile = clientV1.uploadFile(targetPath, DbxWriteMode.add(), file.length(), inputStream);
            System.out.println("Uploaded: " + uploadedFile.toString());
        }
        finally {
            inputStream.close();
        }
    }

    public void informationAboutTheFile (DbxClientV1 client) throws DbxException {
        this.clientV1= client;
        DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");
        System.out.println("Files in the root path:");
        for (DbxEntry child : listing.children) {
            System.out.println("	" + child.name + ": " + child.toString());
        }
    }
}
