package telegram.teamjob.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "records")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long recordId;
    private String lifeRecord;
    private Long chatId;
    private LocalDateTime dateTime;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Record() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getLifeRecord() {
        return lifeRecord;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setLifeRecord(String lifeRecord) {
        this.lifeRecord = lifeRecord;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(recordId, record.recordId) && Objects.equals(lifeRecord, record.lifeRecord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordId, lifeRecord);
    }

    @Override
    public String toString() {
        return "Record{" +
                "recordId=" + recordId +
                ", lifeRecord='" + lifeRecord + '\'' +
                '}';
    }
}
