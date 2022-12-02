package telegram.teamjob.service;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;
import telegram.teamjob.implementation.UserService;
import telegram.teamjob.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import telegram.teamjob.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getStringUser(Update update) {
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
    @Override
    public User createUser(Update update) {
        logger.info("Creating new User");
        return userRepository.save(getStringUser(update));
    }
    @Override
    public User getUser(Long chatId) {
        return userRepository.findByChatId(chatId);
    }


}

