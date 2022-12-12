package telegram.teamjob.implementation;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import telegram.teamjob.service.UserService;
import telegram.teamjob.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import telegram.teamjob.repositories.UserRepository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static telegram.teamjob.constants.BotButtonForShelterMenuEnum.BOT_ANSWER_NOT_SAVED_INFO;
import static telegram.teamjob.constants.BotButtonForShelterMenuEnum.BOT_ANSWER_NOT_SAVED_INFO_LOG;
import static telegram.teamjob.constants.BotMessageEnum.SAVE_INFORMATION;
@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    private TelegramBot telegramBot;
    static private final String USER_TEXT_PATTERN = "([\\W+]+)(\\s)([0-9\\\\\\\\\\\s]{11})(\\s)([\\W+]+)";
    static private final Pattern pattern = Pattern.compile(USER_TEXT_PATTERN);
    public UserServiceImpl(TelegramBot tgBot, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.telegramBot = tgBot;
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

    /**
     * метод проверяет сообщение от пользователя на соответствие шаблону и преобразует его для внесения
     * в БД  путем вызова метода репозитория {@link UserRepository#save(Object)} <br>
     * если сообщение было введено пользователем не верно - бот вернет ответ - что данные не сохранились, так
     * как сообщение не соответсвует шаблону
     * так же метод проверяет есть ли уже такой контакт в БД путем вызова метода репозитория <br>
     * {@link UserRepository #findAllUsersByChatId(long)} , <br>
     * если контакт ранее уже был внесен в БД - бот сообщает об этом пользователю  <br>
     * @param update
     */
    public void createUser(Update update){
        String text = update.message().text();
        long chatId = update.message().chat().id();
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            String pet = matcher.group(1);
            String phone = matcher.group(3);
            String name = matcher.group(5);
            User user = new User();
            user.setNumberPhone(phone);
            user.setUserName(name);
            user.setChatId(chatId);
            user.setPetName(pet);

            List<User> userFromBase = userRepository.findAllUsersByChatId(chatId);
            logger.info(String.valueOf(userFromBase));
            try{
                if(userFromBase.isEmpty()){
                    userRepository.save(user);
                    telegramBot.execute(new SendMessage(chatId, SAVE_INFORMATION.getMessage()));
                    logger.info("Данные сохранены в базе данных");
                } else{
                    logger.info("такой пользователь уже есть в базе данных");
                    telegramBot.execute(new SendMessage(chatId, "Такой пользователь уже есть в базе данных"));
                }
            }
            catch(NullPointerException e){
                logger.info("пользователь предоставил информацию о себе");
            }
        }
        else {

            telegramBot.execute(new SendMessage(chatId, BOT_ANSWER_NOT_SAVED_INFO.getText()));
            logger.warn(BOT_ANSWER_NOT_SAVED_INFO_LOG.getText());
        }

    }
    //  @Override
    //  public User createUser(Update update) {
    //      logger.info("Creating new User");
    //     return userRepository.save(getStringUser(update));
    //  }
    @Override
    public User getUser(Long chatId) {
        return userRepository.findByChatId(chatId);
    }


}
