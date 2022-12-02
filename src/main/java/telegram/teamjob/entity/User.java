package telegram.teamjob.entity;



import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String userName;
    private String numberPhone;
    private long chatId;
    private String petName;

    public User(){}

    public User(Long userId, String userName, String numberPhone, long chatId, String petName, Record userRecord) {
        this.userId = userId;
        this.userName = userName;
        this.numberPhone = numberPhone;
        this.chatId = chatId;
        this.petName = petName;
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

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return chatId == user.chatId && Objects.equals(userId, user.userId) && Objects.equals(userName, user.userName) && Objects.equals(numberPhone, user.numberPhone) && Objects.equals(petName, user.petName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, numberPhone, chatId, petName);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + userName + '\'' +
                ", numberPhone='" + numberPhone + '\'' +
                ", chatId=" + chatId +
                ", petName='" + petName + '\'' +
                '}';
    }
}

