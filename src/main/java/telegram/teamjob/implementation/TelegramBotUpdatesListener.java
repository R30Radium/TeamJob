package telegram.teamjob.implementation;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import telegram.teamjob.constants.BotMessageEnum;
import telegram.teamjob.entity.Record;
import org.springframework.stereotype.Service;
import telegram.teamjob.entity.Contact;
import telegram.teamjob.entity.InformationForOwner;
import telegram.teamjob.entity.Shelter;
import telegram.teamjob.handler.button_answers.clientMessageHandler.MessageHendlerClient;
import telegram.teamjob.repositories.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;


import static telegram.teamjob.constants.BotMessageEnum.*;

/**
 * @author shulga_ea <br>
 * методы сервиса отвечают за: <br>
 * - начало работы с ботом <br>
 * - предоставление пользователю меню с инфорацией о приюте <br>
 * - вызов волонтера <br>
 * - сохранение контаных данных о пользователе для обратной связи <br>
 * <i>за вышеуказанный функционал отвечатю:</i><br>
 * <b>сущности:</b>  {@code Shelter, Contact} + две базы данных <br>
 * <b>репозитории</b>: {@code ShelterRepository, ContactRepository}
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final UserRepository userRepository;
    private final RecordRepository recordRepository;

    private final TelegramBot telegramBot;
    private final UserServiceImpl userService;

    private final RecordServiceImpl recordService;


    private final VolunteerServiceImpl volunteerService;

    private final ContactServiceImpl contactService;


    private final ClientServiceImpl clientService;
    @Autowired
    private MessageHendlerClient messageHendlerClient;

    /**
     * константа для шаблона
     *
     * @see Contact
     */
    static public final String CONTACT_TEXT_PATTERN = "([0-9]{11})(\\s)([\\W+]+)";
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    static private final String exampleContact = "89993334466 Иванов Иван Иванович";

    public TelegramBotUpdatesListener(UserRepository userRepository, RecordRepository recordRepository,
                                      TelegramBot telegramBot,
                                      UserServiceImpl userService,
                                      RecordServiceImpl recordService,
                                      VolunteerServiceImpl volunteerService, ContactServiceImpl contactService,
                                      ClientServiceImpl clientService) {
        this.userRepository = userRepository;
        this.recordRepository = recordRepository;
        this.telegramBot = telegramBot;
        this.userService = userService;
        this.recordService = recordService;
        this.volunteerService = volunteerService;
        this.contactService = contactService;
        this.clientService = clientService;
    }


    @PostConstruct
    public void init() throws IOException {
        String json = Files.readString(Paths.get("update.json"));
        Update update = BotUtils.parseUpdate(json);
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            try {
                messageHandler(update);
            } catch (Exception e) {
                logger.info("Информация введена не корректно: " + e);
                telegramBot.execute(new SendMessage(update.message().chat().id(),
                        "Внимание! Информация введена не кооректно"));
            }
        });
        logger.info("вернул ответ" + UpdatesListener.CONFIRMED_UPDATES_ALL);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * если пользователь решил начать общение, метод обрабатывает команду /start и вызывает метод<br>
     * {@code sendGreetingMessage(update)} <br>
     *
     * @param update
     */
    public void messageHandler(Update update) {
        logger.info("Processing messageHandler");
        if (update.callbackQuery() != null) {
            Long chatId = update.callbackQuery().message().chat().id();
            logger.info("messageHandler - case: callbackQuery");
            messageHendlerClient.startMessage(update);
            if (clientService.findClient(chatId) != null) {
                if (clientService.findClient(chatId).getStatus() == 1) {
                    logger.info("messageHandler - Dogs Client");
                    messageHendlerClient.checkMessageFromDogClient(update);
                } else if (clientService.findClient(chatId).getStatus() == 2l) {
                    logger.info("messageHandler - Cats Client");
                    messageHendlerClient.checkMessageFromCatClient(update);
                }
            }
        } else {
            checkAnswer(update);
        }
    }

    public void checkAnswer(Update update) {
        logger.info("Processing checkAnswer:");
        Long chatId = update.message().chat().id();
        String inputText = update.message().text();
        if (inputText != null) {
            if (inputText.equals("/start")) {
                sendGreetingMessageStatus(update);
            } else if (inputText.equals("/createuser")) {
                telegramBot.execute(sendMessage(chatId, BotMessageEnum.CREATE_USER_INFO.getMessage()));
            } else if (inputText.matches(CONTACT_TEXT_PATTERN)) {
                logger.info("Processing checkAnswer, case: create contatct:");
                if (userRepository.findAllUsersByChatId(chatId).isEmpty()) {
                    contactService.saveContact(update);
                    userService.createUser(update);
                    logger.info("save contact");
                } else {
                    logger.info("User не внесён в БД - users");
                    telegramBot.execute(sendMessage(chatId, USER_NOT_FOUND_MESSAGE.getMessage()));
                }
            } else if (inputText.contains("/record")) {
                logger.info("Отправил инфомарцию об отчете");
                String messageForRecords = BotMessageEnum.DAILY_RECORD_INFO.getMessage();
                telegramBot.execute(sendMessage(chatId, messageForRecords));
            } else if (inputText.contains("Отчёт за")) {
                logger.info("Вызвал метод для сохранения текстового отчета");
                if (userRepository.findAllUsersByChatId(chatId).isEmpty()) {
                    logger.info("Контакта нет в списке");
                    String needUser = USER_NOT_FOUND_MESSAGE.getMessage();
                    telegramBot.execute(sendMessage(chatId, needUser));
                } else {
                    recordService.saveRecord(update);
                }
            } else if (inputText.contains(DELETE_COMMAND.getMessage()))
                contactService.deleteAllContacts(update);
        } else {
            telegramBot.execute(sendMessage(chatId, ASK_HELP.getMessage()));
        }
    }

    /**
     * метод возвращает пользователю приветственное сообшение и <br>
     * кнопки основного меню <br>
     */
    public void sendGreetingMessageStatus(Update update) {
        try {
            String greetingMessage = "@" + update.message().chat().username() + START_MESSAGE.getMessage();
            telegramBot.execute(new SendMessage(update.message().chat().id(), greetingMessage)
                    .replyMarkup(volunteerService.userStatusMenuButtons()));
            logger.info("Отправил меню для определения статуса входа");
        } catch (NullPointerException e) {
            logger.info("Вернулся не корректный ответ");
        }
    }


    private SendMessage sendMessage(long chatId, String textToSend) {
        return new SendMessage(chatId, textToSend);
    }


    /**
     * метод отправляет пользователю список разделов меню с кратким описанием сути каждого раздела <br>
     * за каждый раздел меню отвечает соотствующая константа
     *
     * @param
     * @see Shelter
     * @see Contact
     * @see InformationForOwner
     */


    @Scheduled(cron = "0 0/1 * /1")
    public void checkAvailabilityOfReportAndSendReminder(long chatId, LocalDateTime dateTime) {
        List<Record> records = recordRepository.findAllRecordByChatIdAndDateTime(chatId, dateTime);
        if (records.isEmpty()) {
            telegramBot.execute(new SendMessage(chatId, REPORT_REMEMBER.getMessage() + dateTime.toString()));
            logger.info("Пользователю направлено напоминание");
        }
    }

}




