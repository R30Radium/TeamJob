package telegram.teamjob.entity;

import telegram.teamjob.repositories.ShelterRepository;

import javax.persistence.*;

/**
 * @author shulga_ea <br>
 * поля сущности Shelter - информация для пользователя о приюте <br>
 * инфорация содержится в БД, к которой обращаемся через бота
 * @see ShelterRepository
 */
@Entity
public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    /**
     * поле содержит информацию о приюте
     */
    private String informationAboutShelter;
    /**
     * поле содержит информацию о графике работы приюта
     */
    private String workScheduleShelter;
    /**
     * поле содержит адрес приюта
     */
    private String addressShelter;
    /**
     * поле содержит информацию схеме проезда к приюту
     */
    private String drivingDirectionsShelter;
    /**
     * поле содержит информацию о правилах безопасного поведения в приюте
     */
    private String safetyAtShelter;

    public Shelter(){}

    public Integer getId(){
        return id;
    }

    public void setId(Integer Id){
        this.id=id;
    }

    public String getInformationAboutShelter() {
        return informationAboutShelter;
    }

    public void setInformationAboutShelter(String informationAboutShelter) {
        this.informationAboutShelter = informationAboutShelter;
    }

    public String getWorkScheduleShelter() {
        return workScheduleShelter;
    }

    public void setWorkScheduleShelter(String workScheduleShelter) {
        this.workScheduleShelter = workScheduleShelter;
    }

    public String getAddressShelter() {
        return addressShelter;
    }

    public void setAddressShelter(String addressShelter) {
        this.addressShelter = addressShelter;
    }

    public String getDrivingDirectionsShelter() {
        return drivingDirectionsShelter;
    }

    public void setDrivingDirectionsShelter(String drivingDirectionsShelter) {
        this.drivingDirectionsShelter = drivingDirectionsShelter;
    }

    public String getSafetyAtShelter() {
        return safetyAtShelter;
    }

    public void setSafetyAtShelter(String safetyAtShelter) {
        this.safetyAtShelter = safetyAtShelter;
    }
}

