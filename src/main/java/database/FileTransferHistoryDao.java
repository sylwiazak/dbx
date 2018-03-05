package database;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class FileTransferHistoryDao {

    private Session session;
    private Transaction transaction;

    public FileTransferHistoryDao(Session session) {
        this.session = session;
    }

    public void addFile(String fileName) {
        transaction = session.beginTransaction();
        FileTransferHistory file = new FileTransferHistory();
        file.setName(fileName);
        file.setSendingDate(LocalDate.now());
        session.save(file);
        session.flush();
        transaction.commit();
    }

    public void addFile(String fileName, Long sendingTime) {
        transaction = session.beginTransaction();
        FileTransferHistory file = new FileTransferHistory();
        file.setName(fileName);
        file.setSendingDate(LocalDate.now());
        file.setSendingTime(sendingTime);
        session.save(file);
        session.flush();
        transaction.commit();
    }

    public void updateFile(String fileName, Long sendingTime) {
        transaction = session.beginTransaction();
        FileTransferHistory file = (FileTransferHistory) session
                .createQuery("FROM FileTransferHistory f WHERE f.name = :fileName")
                .setParameter("fileName", fileName).getSingleResult();
        file.setSendingTime(sendingTime);
        file.setUpdateDate(LocalDate.now());
        session.saveOrUpdate(file);
        session.flush();
        transaction.commit();
    }

    public Optional<String> getNewFile(String someFileName) {
        return Optional.ofNullable((String) session.createQuery("select name from FileTransferHistory where name in (:someFileName)")
                .setParameter("someFileName", someFileName)
                .uniqueResult());
    }
}