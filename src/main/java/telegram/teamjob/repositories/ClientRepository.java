package telegram.teamjob.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import telegram.teamjob.entity.Client;
import telegram.teamjob.entity.Contact;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findByChatId(Long chatId);

}
