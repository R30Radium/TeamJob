package telegram.teamjob.entity.Cat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ShelterForCat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    /**
     * поле содержит информацию о приюте для кошек
     */
    private String informationAboutShelterForCat;
    /**
     * поле содержит информацию о графике работы приюта для кошек
     */
    private String workScheduleShelterForCat;
    /**
     * поле содержит адрес приюта для кошек
     */
    private String addressShelterForCat;

    /**
     * поле содержит информацию схеме проезда к приюту для кошек
     */
   private String  securityContact;
    /**
     * поле содержит контактные данные охраны для оформления пропуска на машину для проезда на территорию приюта для кошек
     */

    private String drivingDirectionsShelterForCat;
    /**
     * поле содержит информацию о правилах безопасного поведения в приюте для кошек
     */
    private String safetyAtShelterForCat;

    public ShelterForCat(){}

    public Integer getId(){
        return id;
    }

    public void setId(Integer Id){
        this.id=id;
    }

    public String getInformationAboutShelterForCat() {
        return informationAboutShelterForCat;
    }

    public void setInformationAboutShelterForCat(String informationAboutShelterForCat) {
        this.informationAboutShelterForCat = informationAboutShelterForCat;
    }

    public String getWorkScheduleShelterForCat() {
        return workScheduleShelterForCat;
    }

    public void setWorkScheduleShelterForCat(String workScheduleShelterForCat) {
        this.workScheduleShelterForCat = workScheduleShelterForCat;
    }

    public String getAddressShelterForCat() {
        return addressShelterForCat;
    }

    public void setAddressShelterForCat(String addressShelterForCat) {
        this.addressShelterForCat = addressShelterForCat;
    }

    public String getDrivingDirectionsShelterForCat() {
        return drivingDirectionsShelterForCat;
    }

    public void setDrivingDirectionsShelterForCat(String drivingDirectionsShelterForCat) {
        this.drivingDirectionsShelterForCat = drivingDirectionsShelterForCat;
    }

    public String getSafetyAtShelterForCat() {
        return safetyAtShelterForCat;
    }

    public void setSafetyAtShelterForCat(String safetyAtShelterForCat) {
        this.safetyAtShelterForCat = safetyAtShelterForCat;
    }
    public String getSecurityContact() {
        return securityContact;
    }

    public void setSecurityContact(String securityContact) {
        this.securityContact = securityContact;
    }
}


