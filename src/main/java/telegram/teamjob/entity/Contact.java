package telegram.teamjob.entity;

import telegram.teamjob.repositories.ContactRepository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author shulga_ea <br>
 * сущность Сontact - ФИО и телефон, которые пользователь предоставляет, для обратной связи с ним<br>
 * id записи в БД генерируется автоматически <br>
 * бот заносит данные в БД <br>
 * @see ContactRepository
 *
 */
@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String numberPhone;

    private LocalDateTime dateTime;

    public Contact(){}

    public LocalDateTime getLocalDateTime() {
        return dateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.dateTime = localDateTime;
    }

    public Contact(int id, String numberPhone, String name){
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
 /*public String getTimeForCalling(){
        return timeForCalling;
 }
 public void setTimeForCalling(String timeForCalling){
        this.timeForCalling=timeForCalling;
 }

  */

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
        Contact contact = (Contact) o;
        return  name == contact.getName() && Objects.equals(id, contact.id) && Objects.equals(numberPhone, contact.numberPhone);
    }


    @Override
    public int hashCode(){
        return Objects.hash(id, name, numberPhone);
    }
}
