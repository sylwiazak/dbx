package database;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("unchecked")
public class FileTransferHistoryDao {

    private Session session;
    private Transaction transaction;

    public FileTransferHistoryDao(Session session) {
        this.session = session;
    }

    public void addFile(String fileName){
        transaction= session.beginTransaction();
        FileTransferHistory file= new FileTransferHistory();
        file.setName(fileName);
        file.setSendingDate(LocalDate.now());
        session.save(file);
        session.flush();
        transaction.commit();
    }

    public void addFile(String fileName, Long sendingTime){
        transaction= session.beginTransaction();
        FileTransferHistory file= new FileTransferHistory();
        file.setName(fileName);
        file.setSendingDate(LocalDate.now());
        file.setSendingTime(sendingTime);
        session.save(file);
        session.flush();
        transaction.commit();
    }

    public void updateFile(String fileName, Long sendingTime){
        transaction= session.beginTransaction();
        System.out.println("update 1");
        FileTransferHistory file= (FileTransferHistory) session
                .createQuery("FROM FileTransferHistory f WHERE f.name = :fileName")
                .setParameter("fileName", fileName).getSingleResult();
        file.setSendingTime(sendingTime);
        file.setUpdateDate(LocalDate.now());
        session.saveOrUpdate(file);
        session.flush();
        transaction.commit();
    }

    public List<String> getListFile(){
        return session.createQuery("select name from FileTransferHistory").getResultList();
    }
}
