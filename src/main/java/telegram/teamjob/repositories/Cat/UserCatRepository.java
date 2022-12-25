package telegram.teamjob.repositories.Cat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram.teamjob.entity.Cat.UserCat;


import java.util.List;

@Repository
public interface UserCatRepository extends JpaRepository<UserCat, Long> {

    UserCat findByChatId(Long chatId);
    List<UserCat> findAllUserCatByChatId(Long chatId);
}