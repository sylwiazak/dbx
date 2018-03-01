package database;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "file")
@Access(AccessType.PROPERTY)
public class FileTransferHistory {

    private Long id;
    private String name;
    private LocalDate sendingDate;
    private Long sendingTime;
    private LocalDate updateDate;

    public FileTransferHistory() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "filename")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "sending_date")
    public LocalDate getSendingDate() {
        return sendingDate;
    }

    public void setSendingDate(LocalDate sendingDate) {
        this.sendingDate = sendingDate;
    }

    @Column(name = "sending_time")
    public Long getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(Long sendingTime) {
        this.sendingTime = sendingTime;
    }

    @Column(name = "update_date")
    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }
}
