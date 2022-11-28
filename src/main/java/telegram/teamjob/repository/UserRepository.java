package telegram.teamjob.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telegram.teamjob.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByChatId(Long chatId);

}
