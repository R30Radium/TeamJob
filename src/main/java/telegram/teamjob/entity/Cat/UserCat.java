package telegram.teamjob.entity.Cat;

import telegram.teamjob.entity.Record;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "userscat")
public class UserCat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String userName;
    private String numberPhone;
    private long chatId;
    private String catName;

    public UserCat(){

    }

    public UserCat(Long userId, String userName, String numberPhone, long chatId, String catName, RecordCat userCatRecord) {
        this.userId = userId;
        this.userName = userName;
        this.numberPhone = numberPhone;
        this.chatId = chatId;
        this.catName = catName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getUserName(){
        return userName;
    }
    public void setUserName(String name){
        this.userName = name;
    }

    public String getNumberPhone(){
        return numberPhone;
    }
    public void setNumberPhone(String numberPhone){
        this.numberPhone = numberPhone;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String petName) {
        this.catName = catName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
       UserCat userCat = (UserCat) o;
        return chatId == userCat.chatId && Objects.equals(userId, userCat.userId) && Objects.equals(userName, userCat.userName) && Objects.equals(numberPhone, userCat.numberPhone) && Objects.equals(catName, userCat.catName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, numberPhone, chatId, catName);
    }

    @Override
    public String toString() {
        return "UserCat{" +
                "userId=" + userId +
                ", name='" + userName + '\'' +
                ", numberPhone='" + numberPhone + '\'' +
                ", chatId=" + chatId +
                ", catName='" + catName + '\'' +
                '}';
    }
}
