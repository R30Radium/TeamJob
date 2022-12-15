package telegram.teamjob.implementation;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
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
import static telegram.teamjob.implementation.TelegramBotUpdatesListener.CONTACT_TEXT_PATTERN;

@Service
public class UserServiceImpl {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    private TelegramBot telegramBot;
    static private final Pattern pattern = Pattern.compile(CONTACT_TEXT_PATTERN);
    public UserServiceImpl(TelegramBot tgBot, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.telegramBot = tgBot;
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
        logger.info("Создание пользователя");
        String text = update.message().text();
        long chatId = update.message().chat().id();
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            String phone = matcher.group(1);
            String name = matcher.group(3);
            User user = new User();
            user.setNumberPhone(phone);
            user.setUserName(name);
            user.setChatId(chatId);

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
                logger.info("пользователь не предоставил информацию о себе"+e);
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

    public User getUser(Long chatId) {
        return userRepository.findByChatId(chatId);
    }


}
