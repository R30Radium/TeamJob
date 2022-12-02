package telegram.teamjob.repositories;

import org.springframework.stereotype.Repository;
import telegram.teamjob.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository <User, Long>{

    User findByChatId(Long chatId);
}