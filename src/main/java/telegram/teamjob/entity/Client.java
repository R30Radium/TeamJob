package telegram.teamjob.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private long chatId;
    private int status;

    public Client() {
    }

    public Client(Long id, long chatId, int status) {
        this.id = id;
        this.chatId = chatId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long userId) {
        this.id = userId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return chatId == client.chatId && status == client.status && Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, status);
    }

    @Override
    public String
    toString() {
        return "Client{" +
                "userId=" + id +
                ", chatId=" + chatId +
                ", status=" + status +
                '}';
    }
}
