package telegram.teamjob.entity.Cat;

import telegram.teamjob.entity.Volunteer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;
@Entity
public class VolunteerCat {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long idVolunteer;

    private String nameVolunteer;

    public Long getIdVolunteer() {
        return idVolunteer;
    }

    public void setIdVolunteer(Long idVolunteer) {
        this.idVolunteer = idVolunteer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VolunteerCat volunteerCat = (VolunteerCat) o;
        return Objects.equals(idVolunteer, volunteerCat.idVolunteer) && Objects.equals(nameVolunteer, volunteerCat.nameVolunteer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVolunteer, nameVolunteer);
    }

    @Override
    public String toString() {
        return "VolunteerCat{" +
                "idVolunteer=" + idVolunteer +
                ", nameVolunteer='" + nameVolunteer + '\'' +
                '}';
    }

}
