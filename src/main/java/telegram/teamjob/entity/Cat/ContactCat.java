package telegram.teamjob.entity.Cat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class ContactCat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String numberPhone;

    private LocalDateTime dateTime;

    public ContactCat(){}

    public LocalDateTime getLocalDateTime() {
        return dateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.dateTime = localDateTime;
    }

    public ContactCat(int id, String numberPhone, String name){
        this.id=id;
        this.numberPhone = numberPhone;
        this.name = name;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getNumberPhone(){
        return numberPhone;
    }
    public void setNumberPhone(String numberPhone){
        this.numberPhone = numberPhone;
    }


    @Override
    public String toString() {
        return "Контакт пользователя: " +"\n"+
                " id = " + id + "\n"+
                " имя  = " + name  + "\n"+
                " телефон = " + numberPhone  + "\n"+
                " дата внесения записи = " + dateTime + "; " + "\n";
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        ContactCat contactCat = (ContactCat) o;
        return  name == contactCat.getName() && Objects.equals(id, contactCat.id) && Objects.equals(numberPhone, contactCat.numberPhone);
    }


    @Override
    public int hashCode(){
        return Objects.hash(id, name, numberPhone);
    }
}



