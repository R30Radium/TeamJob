package telegram.teamjob.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Report {

    @GeneratedValue
    @Id
    private Long reportId;

    @OneToOne
    private PetPhoto petPhoto;


    public Report() {
    }


    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public PetPhoto getPetPhoto() {
        return petPhoto;
    }

    public void setPetPhoto(PetPhoto petPhoto) {
        this.petPhoto = petPhoto;
    }

    }
