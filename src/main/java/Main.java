import database.Config;
import dropBox.DropBoxService;
import dropBox.WatcherService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {

        String accessToken = args[0];
        final String user = args[1];
        final String pass = args[2];
        final String url = args[3];

        Config config = new Config(user, pass, url);
        SessionFactory sessionFactory = config.sessionFactory();
        Session session = sessionFactory.openSession();
        WatcherService watcherService = new WatcherService(new DropBoxService(accessToken), session);
        try {
            watcherService.watch("src\\main\\resources\\source");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}