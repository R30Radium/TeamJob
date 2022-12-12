package telegram.teamjob.repositories;

import org.springframework.stereotype.Repository;
import telegram.teamjob.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository <User, Long>{

    User findByChatId(Long chatId);
    List<User> findAllUsersByChatId(Long chatId);
}