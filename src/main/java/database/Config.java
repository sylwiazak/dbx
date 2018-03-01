package database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import static org.hibernate.cfg.AvailableSettings.*;

public class Config {

    private String user;
    private String password;
    private String url;

    public Config(String user, String password, String url) {
        this.user = user;
        this.password = password;
        this.url = url;
    }

    public SessionFactory sessionFactory() {
        return new Configuration()
                .addAnnotatedClass(database.FileTransferHistory.class)
                .setProperty(Environment.DRIVER, "org.postgresql.Driver")
                .setProperty(URL, url)
                .setProperty(USER, user)
                .setProperty(PASS, password)
                .setProperty(DIALECT, "org.hibernate.dialect.PostgreSQL9Dialect")
                .setProperty(CURRENT_SESSION_CONTEXT_CLASS, "thread")
                .setProperty(SHOW_SQL, Boolean.TRUE.toString())
                .setProperty(HBM2DDL_AUTO, "update")
                .buildSessionFactory();
    }
}
