package telegram.teamjob.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Report {

    @GeneratedValue
    @Id
    private Long reportId;

    private String diet;
    private String adaptions;
    private String changeInBehavior;


    @OneToOne
    private PetPhoto petPhoto;

    @OneToMany(mappedBy = "report")
    @JsonIgnore
    private Collection<User> usersOfTheReport;

    public Collection<User> getUsersOfTheReport() {
        return usersOfTheReport;
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

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getAdaptions() {
        return adaptions;
    }

    public void setAdaptions(String adaptions) {
        this.adaptions = adaptions;
    }

    public String getChangeInBehavior() {
        return changeInBehavior;
    }

    public void setChangeInBehavior(String changeInBehavior) {
        this.changeInBehavior = changeInBehavior;
    }
}
