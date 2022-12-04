package telegram.teamjob.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="petphotos")
public class PetPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long petPhotosId;

    private String filePath;
    private long fileSize;
    @ManyToOne
    @JoinColumn(name = "record_id")
    private Record record;


    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }


    public PetPhoto() {
    }

    public Long getPetPhotosId() {
        return petPhotosId;
    }

    public void setPetPhotosId(Long petPhotosId) {
        this.petPhotosId = petPhotosId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetPhoto petPhoto = (PetPhoto) o;
        return fileSize == petPhoto.fileSize && Objects.equals(petPhotosId, petPhoto.petPhotosId) && Objects.equals(filePath, petPhoto.filePath) && Objects.equals(record, petPhoto.record);
    }

    @Override
    public int hashCode() {
        return Objects.hash(petPhotosId, filePath, fileSize, record);
    }

    @Override
    public String toString() {
        return "PetPhoto{" +
                "petPhotosId=" + petPhotosId +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }

}