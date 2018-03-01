import database.Config;
import database.FileTransferHistoryDao;
import dropBox.DropBoxService;
import dropBox.WatcherService;
import folder.Folder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;


public class Main {

    private static final String SOURCE = "src\\main\\resources\\source";

    public static void main(String[] args) {

        String accessToken = args[0];
        final String user = args[1];
        final String pass = args[2];
        final String url = args[3];

        Config config = new Config(user, pass, url);
        SessionFactory sessionFactory = config.sessionFactory();
        Session session = sessionFactory.openSession();
        FileTransferHistoryDao fileTransferHistoryDao = new FileTransferHistoryDao(session);
        WatcherService watcherService = new WatcherService(new DropBoxService(accessToken), fileTransferHistoryDao);
        Folder folder = new Folder(fileTransferHistoryDao);

        try {
            folder.checkNewFiles(SOURCE);
            watcherService.watch(SOURCE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}