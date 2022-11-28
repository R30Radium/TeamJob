package telegram.teamjob.Service;

import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.entity.User;
import telegram.teamjob.repository.UserRepository;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    private User getStringUser(Update update) {
        logger.info("getStringUser method");
        Long chatId = update.message().chat().id();

        String message = update.message().text();

        String[] words = message.split(" ");
        String userName = words[0];
        String userNumber = words[1];
        String userPetName = words[2];

        // Проверка на переданное сообщение
        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setNumberPhone(userNumber);
        newUser.setPetName(userPetName);
        newUser.setChatId(chatId);
        return newUser;
    }

    public User createUser(Update update) {
        logger.info("Creating new User");
        return userRepository.save(getStringUser(update));
    }

    public User getUser(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

}
