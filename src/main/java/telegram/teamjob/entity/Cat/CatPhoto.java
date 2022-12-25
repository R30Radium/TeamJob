package telegram.teamjob.entity.Cat;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="catphotos")
public class CatPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long catPhotosId;

    private String filePath;
    private long fileSize;
    @ManyToOne
    @JoinColumn(name = "record_id")
    private RecordCat recordCat;

    public CatPhoto() {
    }

    public Long getCatPhotosId() {
        return catPhotosId;
    }

    public void setCatPhotosId(Long catPhotosId) {
        this.catPhotosId = catPhotosId;
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

    public RecordCat getRecordCat() {
        return recordCat;
    }

    public void setRecordCat(RecordCat recordCat) {
        this.recordCat = recordCat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatPhoto catPhoto = (CatPhoto) o;
        return fileSize == catPhoto.fileSize && Objects.equals(catPhotosId, catPhoto.catPhotosId) && Objects.equals(filePath, catPhoto.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catPhotosId, filePath, fileSize);
    }
}