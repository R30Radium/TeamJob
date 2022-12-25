package telegram.teamjob.implementation.Cat;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.entity.Cat.UserCat;
import telegram.teamjob.repositories.Cat.UserCatRepository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static telegram.teamjob.constants.BotButtonForShelterMenuEnum.BOT_ANSWER_NOT_SAVED_INFO;
import static telegram.teamjob.constants.BotButtonForShelterMenuEnum.BOT_ANSWER_NOT_SAVED_INFO_LOG;
import static telegram.teamjob.constants.BotMessageEnum.SAVE_INFORMATION;
import static telegram.teamjob.implementation.TelegramBotUpdatesListener.CONTACT_TEXT_PATTERN;

@Service
public class UserCatServiceImpl {

    private Logger logger = LoggerFactory.getLogger(UserCatServiceImpl.class);
    private final UserCatRepository userCatRepository;

    private TelegramBot telegramBot;
    static private final Pattern pattern = Pattern.compile(CONTACT_TEXT_PATTERN);
    public UserCatServiceImpl(TelegramBot tgBot, UserCatRepository userCatRepository) {
        this.userCatRepository = userCatRepository;
        this.telegramBot = tgBot;
    }


    /**
     * метод проверяет сообщение от пользователя на соответствие шаблону и преобразует его для внесения
     * в БД  путем вызова метода репозитория {@link UserCatRepository#save(Object)} <br>
     * если сообщение было введено пользователем не верно - бот вернет ответ - что данные не сохранились, так
     * как сообщение не соответсвует шаблону
     * так же метод проверяет есть ли уже такой контакт в БД путем вызова метода репозитория <br>
     * {@link UserCatRepository #findAllUserCatByChatId(long)} , <br>
     * если контакт ранее уже был внесен в БД - бот сообщает об этом пользователю  <br>
     * @param update
     */
    public void createUserCat(Update update){
        logger.info("Создание пользователя");
        String text = update.message().text();
        long chatId = update.message().chat().id();
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            String phone = matcher.group(1);
            String name = matcher.group(3);
            UserCat userCat = new UserCat();
            userCat.setNumberPhone(phone);
            userCat.setUserName(name);
            userCat.setChatId(chatId);

            List<UserCat> userFromBase = userCatRepository.findAllUserCatByChatId(chatId);
            logger.info(String.valueOf(userFromBase));
            try{
                if(userFromBase.isEmpty()){
                    userCatRepository.save(userCat);
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

    public List<UserCat> findUsers(Long chatId) {
        return userCatRepository.findAllUserCatByChatId(chatId);
    }

    public UserCat getUserCat(Long chatId) {
        return userCatRepository.findByChatId(chatId);
    }


}
