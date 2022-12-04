package telegram.teamjob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telegram.teamjob.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByChatId(Long chatId);

}
