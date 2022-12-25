package telegram.teamjob.entity.Cat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
    @Table(name = "recordscat")
    public class RecordCat {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long recordCatId;
        private Long chatId;
        private LocalDateTime dateTime;
        private String diet;
        private String adaptation;
        private String changeInBehavior;

        public RecordCat(Long recordCatId, Long chatId, LocalDateTime dateTime, String diet, String adaptation, String changeInBehavior) {
            this.recordCatId = recordCatId;
            this.chatId = chatId;
            this.dateTime = dateTime;
            this.diet = diet;
            this.adaptation = adaptation;
            this.changeInBehavior = changeInBehavior;
        }

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

    public RecordCat() {
    }


    public Long getRecordCatId() {
        return recordCatId;
    }

    public void setRecordCatId(Long recordCatId) {
        this.recordCatId = recordCatId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordCat recordCat = (RecordCat) o;
        return Objects.equals(recordCatId, recordCat.recordCatId) && Objects.equals(chatId, recordCat.chatId) && Objects.equals(dateTime, recordCat.dateTime) && Objects.equals(diet, recordCat.diet) && Objects.equals(adaptation, recordCat.adaptation) && Objects.equals(changeInBehavior, recordCat.changeInBehavior);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordCatId, chatId, dateTime, diet, adaptation, changeInBehavior);
    }

    @Override
    public String toString() {
        return "RecordCat{" +
                "recordCatId=" + recordCatId +
                ", chatId=" + chatId +
                ", dateTime=" + dateTime +
                ", diet='" + diet + '\'' +
                ", adaptions='" + adaptation + '\'' +
                ", changeInBehavior='" + changeInBehavior + '\'' +
                '}';
    }

}
