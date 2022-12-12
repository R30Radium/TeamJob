package telegram.teamjob.entity;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "records")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long recordId;
    private Long chatId;
    private LocalDateTime dateTime;

    private String diet;
    private String adaptation;
    private String changeInBehavior;

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getAdaptation() {
        return adaptation;
    }

    public void setAdaptation(String adaptation) {
        this.adaptation = adaptation;
    }

    public String getChangeInBehavior() {
        return changeInBehavior;
    }

    public void setChangeInBehavior(String changeInBehavior) {
        this.changeInBehavior = changeInBehavior;
    }

    //  @ManyToOne
    // @JoinColumn(name = "user_id")
    //  private User user;

    public Record() {
    }

    //  public User getUser() {
    //      return user;
    //   }

    //  public void setUser(User user) {
    //     this.user = user;
    //  }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }



    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

  /*  @Override
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
    */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(recordId, record.recordId) && Objects.equals(chatId, record.chatId) && Objects.equals(dateTime, record.dateTime) && Objects.equals(diet, record.diet) && Objects.equals(adaptation, record.adaptation) && Objects.equals(changeInBehavior, record.changeInBehavior);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordId, chatId, dateTime, diet, adaptation, changeInBehavior);
    }

    @Override
    public String toString() {
        return "Record{" +
                "recordId=" + recordId +
                ", chatId=" + chatId +
                ", dateTime=" + dateTime +
                ", diet='" + diet + '\'' +
                ", adaptions='" + adaptation + '\'' +
                ", changeInBehavior='" + changeInBehavior + '\'' +
                '}';
    }
}
