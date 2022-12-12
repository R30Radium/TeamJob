package telegram.teamjob.implementation;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import telegram.teamjob.entity.Record;
import org.springframework.stereotype.Service;
import telegram.teamjob.entity.Contact;
import telegram.teamjob.entity.InformationForOwner;
import telegram.teamjob.entity.Shelter;
import telegram.teamjob.repositories.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;


import static telegram.teamjob.constants.BotButtonForShelterMenuEnum.*;
import static telegram.teamjob.constants.BotButtonEnum.BUTTON_CHECK_CONTACT;
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
    private final PetPhotoRepository petPhotoRepository;

    private final TelegramBot telegramBot;
    private final ShelterRepository shelterRepository;
    private final ContactRepository contactRepository;
    private final InformationForOwnerRepository informationForOwnerRepository;
    private final UserServiceImpl userService;

    private final RecordServiceImpl recordService;

    private final PetPhotoServiceImpl petPhotoService;
    private final ReportRepository reportRepository;
    private final VolunteerRepository volunteerRepository;

    private final VolunteerServiceImpl volunteerService;

    private final ContactServiceImpl contactService;

    private final ReportServiceImpl reportService;

    private final ShelterServiceImpl shelterService;

    /**
     * константа для шаблона
     * @see Contact
     */
    static private final String USER_TEXT_PATTERN = "([\\W+]+)(\\s)([0-9\\\\\\\\\\\s]{11})(\\s)([\\W+]+)";
    static private final Pattern pattern = Pattern.compile(USER_TEXT_PATTERN);

    static private final String CONTACT_TEXT_PATTERN = "([0-9\\\\\\\\\\\s]{11})(\\s)([\\W+]+)";
    static private final Pattern pattern2 = Pattern.compile(CONTACT_TEXT_PATTERN);

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private boolean flag;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, ShelterRepository shelterRepository,
                                      ContactRepository contactRepository,
                                      InformationForOwnerRepository informationForOwnerRepository,
                                      UserServiceImpl userService, RecordServiceImpl recordService,
                                      PetPhotoServiceImpl petPhotoService,ReportRepository reportRepository,
                                      PetPhotoRepository petPhotoRepository,
                                      RecordRepository recordRepository,
                                      UserRepository userRepository, VolunteerRepository volunteerRepository,
                                      VolunteerServiceImpl volunteerService,  ContactServiceImpl contactService,
                                      ReportServiceImpl reportService, ShelterServiceImpl shelterService) {
        this.telegramBot = telegramBot;
        this.shelterRepository = shelterRepository;
        this.contactRepository = contactRepository;
        this.informationForOwnerRepository = informationForOwnerRepository;
        this.userService = userService;
        this.recordService = recordService;
        this.petPhotoService = petPhotoService;
        this.reportRepository = reportRepository;
        this.petPhotoRepository = petPhotoRepository;
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
        this.volunteerRepository = volunteerRepository;
        this.volunteerService = volunteerService;
        this.contactService = contactService;
        this.reportService = reportService;
        this.shelterService = shelterService;
    }

    @PostConstruct
    public void init() throws IOException {
        String json = Files.readString(Paths.get("update.json"));
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            try {
                messageHandler(update);
            } catch (Exception e) {
                logger.info("Информация введена не корректно");
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
     * @param update
     */
    public void messageHandler(Update update)  {
        if (update.callbackQuery() != null) {
            logger.info("CallBackQuery processing");
            checkButtonAnswerForStatus(update);
            if(update.callbackQuery().data().equals("Узнать информацию о приюте")||
                    update.callbackQuery().data().equals("Как взять собаку из приюта")||
                    update.callbackQuery().data().equals("Прислать отчет о питомце")||
                    update.callbackQuery().data().equals("Позвать волонтера")){

                checkButtonAnswer(update);
            }
            else if(update.callbackQuery().data().equals("info") || //команды для меню первого этапа
                    update.callbackQuery().data().equals("way") ||
                    update.callbackQuery().data().equals("address") ||
                    update.callbackQuery().data().equals("safety") ||
                    update.callbackQuery().data().equals("volunteer") ||
                    update.callbackQuery().data().equals("workTime")||
                    update.callbackQuery().data().equals("contact") ) {
                sendResponseForFirstAndSecondMenu(update);
            }
            else if(update.callbackQuery().data().equals("rules") ||//команды для меню второго этапа
                    update.callbackQuery().data().equals("docs")||
                    update.callbackQuery().data().equals("transportation)") ||
                    update.callbackQuery().data().equals("arrangementPuppy") ||
                    update.callbackQuery().data().equals("arrangementDog") ||
                    update.callbackQuery().data().equals("arrangementDogInvalid")||
                    update.callbackQuery().data().equals("cynologist") ||
                    update.callbackQuery().data().equals("goodCynologists") ||
                    update.callbackQuery().data().equals("reject")){
                sendResponseForFirstAndSecondMenu(update);
            }
            else if(update.callbackQuery().data().equals("photo") ||//команды для меню третьего этапа(отчет)
                    update.callbackQuery().data().equals("record")){
                sendResponseForThirdMenu(update);
            }
            else if(update.callbackQuery().data().equals(BUTTON_CHECK_CONTACT.getMessage())){
                contactService.getAllContactsForVolunteer(update);
            }
        }
        else if (update.message().photo() != null) {
            logger.info("Photo Upload processing");
            long recordId = recordService.findRecordId(update);
            PhotoSize[] photos = update.message().photo();
            try {
                petPhotoService.uploadPhoto(recordId, photos);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            logger.info("Processing update: {}", update);
            checkAnswer(update);
        }
    }
    public void checkButtonAnswerForStatus(Update update) {
        String callBackData = update.callbackQuery().data();
        logger.info(callBackData);
        int messageId = update.callbackQuery().message().messageId();
        long chatId = update.callbackQuery().message().chat().id();
        switch (callBackData) {
            case "Волонтер":
                //вызов  ответа для волонтера
                telegramBot.execute(new EditMessageText(chatId,messageId, START_MESSAGE_VOLUNTEER.getMessage()).
                        replyMarkup(volunteerService.menuForVolunteer()));
                logger.info("Отправил меню для волонтера");
                break;
            case "Пользователь":
                telegramBot.execute(new EditMessageText(chatId,messageId,
                        START_MESSAGE_USER.getMessage()).replyMarkup(shelterService.ourMenuButtons()));
                logger.info("Отправил меню для пользователя");
                break;
        }
    }


    public void checkAnswer(Update update)  {
        Long chatId = update.message().chat().id();
        String inputText = update.message().text();
        if (inputText != null) {
            if (inputText.equals("/start")) {
                sendGreetingMessageStatus(update);
            }
            else if (inputText.equals("([0-9\\\\\\\\\\\s]{11})(\\s)([\\W+]+)")){
                logger.info("Получил данные для создания контакта");
            }else if(inputText.matches("([0-9\\\\\\\\\\\s]{11})(\\s)([\\W+]+)")){
                contactService.saveContact(update);
                logger.info("вызвал метод для сохранения контакта в базе данных");
            }
            else if (inputText.contains("/record")) {
                recordService.saveRecord(update);
                logger.info("Вызвал метод для сохранения текстового отчета");
                // }
                //  else if ((update.message().photo() != null)) {
                //        logger.info("Photo Upload processing");
                // long recordId = recordService.findRecordId(update);
                //inputText.contains("/photo")) {
                // String messageForRecords = BotMessageEnum.DAILY_RECORD_INFO.getMessage();
                // telegramBot.execute(sendMessage(chatId, messageForRecords));
                //    savePetPhoto(update);
            }
            else if(inputText.contains(DELETE_COMMAND.getMessage()))
                contactService.deleteAllContacts(update);
        } else {
            telegramBot.execute(sendMessage(chatId, ASK_HELP.getMessage()));
        }
    }


    public void sendGreetingMessageStatus(Update update) {
        try {
            String greetingMessage = update.message().chat().username() + START_MESSAGE.getMessage();
            telegramBot.execute(new SendMessage(update.message().chat().id(), greetingMessage)
                    .replyMarkup(volunteerService.userStatusMenuButtons()));
            logger.info("Отправил меню для определения статуса входа");
        }
        catch (NullPointerException e){
            logger.info("Вернулся не корректный ответ");
        }
    }


    /**
     * метод возвращает пользователю приветственное сообшение и <br>
     * кнопки основного меню <br>
     */

    public void sendGreetingMessage(Update update) {
        try {
            String greetingMessage = update.message().chat().username() + START_MESSAGE.getMessage();
            telegramBot.execute(new SendMessage(update.message().chat().id(), greetingMessage)
                    .replyMarkup(shelterService.ourMenuButtons()));
            logger.info("Message Sent" + " Method - sendGreeting");
        }
        catch (NullPointerException e){
            logger.info("Вернулся не корректный ответ");
        }
    }


    private SendMessage sendMessage(long chatId, String textToSend) {
        return new SendMessage(chatId, textToSend);
    }

    /**
     * метод возвращает пользователю один из подразделов главного меню (в зависимости <br>
     * от того, какой раздел главного меню выбрал пользователь)
     * @param update
     */

    public void checkButtonAnswer(Update update) {
        String callBackData = update.callbackQuery().data();
        logger.info(callBackData);
        long chatId = update.callbackQuery().message().chat().id();
        switch (callBackData) {
            case "Узнать информацию о приюте":
                //вызов  меню этапа 1
                String newMessage = ANSWER_FOR_MENU_SHELTER.getMessage();
                telegramBot.execute(new SendMessage(chatId, newMessage)
                        .replyMarkup(shelterService.makeButtonsForMenuStageOne()));
                break;
            case "Как взять собаку из приюта":
                //вызов  меню этапа 2
                String message = ANSWER_FOR_MENU_INFORMATION.getMessage();
                telegramBot.execute(new SendMessage(chatId, message)
                        .replyMarkup(shelterService.makeButtonsForMenuStageTwo()));
                break;
            case "Прислать отчет о питомце":
                //Отчет по питомцу
                String greetingMessage = ANSWER_FOR_MENU_REPORT.getMessage();
                telegramBot.execute(new SendMessage(chatId, greetingMessage)
                        .replyMarkup(reportService.makeButtonsForMenuStageThreeForReport()));
                logger.info("Выбрано меню - отчет о питомце");
                break;
            case "Позвать волонтера":
                telegramBot.execute(new SendMessage(chatId, ASK_HELP.getMessage()));
                break;
        }
    }

    /**
     * метод отправляет пользователю список разделов меню с кратким описанием сути каждого раздела <br>
     * за каждый раздел меню отвечает соотствующая константа
     * @see Shelter
     * @see Contact
     * @see InformationForOwner
     * @param
     */

    public void sendResponseForThirdMenu(Update update) {
        logger.info(" вызван метод sendResponseForThirdMenu");
        String answerMenu = update.callbackQuery().data();
        long chatId = update.callbackQuery().message().chat().id();
        int messageId = update.callbackQuery().message().messageId();
        switch (answerMenu) {
            case "photo":
                telegramBot.execute(new EditMessageText(chatId, messageId, PHOTO.getMessage()));
                logger.warn("IMPORTANT" + PHOTO.getMessage());
                break;
            case "record":
                EditMessageText answer2 = new EditMessageText(chatId, messageId, RECORD.getMessage());
                telegramBot.execute(answer2);
                logger.warn("send info for client about report");
                break;
        }
    }

    public void sendResponseForFirstAndSecondMenu(Update update){
        logger.info("вызвал метод sendAnswer");
        String answerMenu = update.callbackQuery().data();
        long chatId = update.callbackQuery().message().chat().id();
        int messageId = update.callbackQuery().message().messageId();
        logger.info("Ответ от кнопки " + answerMenu);
        List<Shelter> shelters = shelterRepository.findAll();
        List<InformationForOwner> info = informationForOwnerRepository.findAll();
        switch (answerMenu) {
            case "info":
                for (Shelter shelter : shelters) {
                    try {
                        String information = shelter.getInformationAboutShelter();
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                }
                break;
            case "workTime":
                for (Shelter shelter : shelters) {
                    try {
                        String workSchedule = shelter.getWorkScheduleShelter();
                        EditMessageText answer = new EditMessageText(chatId, messageId, workSchedule);
                        logger.warn("IMPORTANT" + workSchedule);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                }
                break;
            case "address":
                for (Shelter shelter : shelters) {
                    String address = shelter.getAddressShelter();
                    EditMessageText answer = new EditMessageText(chatId, messageId, address);
                    logger.warn("IMPORTANT" + address);
                    telegramBot.execute(answer);
                }
                break;
            case "way":
                for (Shelter shelter : shelters) {
                    try {
                        String way = shelter.getDrivingDirectionsShelter();
                        EditMessageText answer = new EditMessageText(chatId, messageId, way);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                }
                break;
            case "safety":
                for (Shelter shelter : shelters) {
                    try {
                        String safety = shelter.getSafetyAtShelter();
                        EditMessageText answer = new EditMessageText(chatId, messageId, safety);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                }
                break;
            case "volunteer":
                telegramBot.execute(new SendMessage(chatId, "Спасибо за обращение, волонтер приюта свяжется " +
                        "с Вами"));
                break;
            case "contact":
                telegramBot.execute(new SendMessage(chatId, COMMAND_SAFE_CONTACT_DETAILS_FOR_COMMUNICATION.getText()));
                break;
            case "rules":
                for (InformationForOwner infoOwner : info) {
                    try {
                        String information = infoOwner.getRules();
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                }
                logger.info("Кнопка 1 работает");
                break;
            case "docs":
                for (InformationForOwner infoOwner : info) {
                    try {
                        String information = infoOwner.getDocs();
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                }
                logger.info("Кнопка 2 работает");
                break;
            case "transpartation":
                for (InformationForOwner infoOwner : info) {
                    try {
                        String information = infoOwner.getTranspartation();
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                }
                logger.info("Кнопка 3 работает");
                break;
            case "arrangementPuppy":
                for (InformationForOwner infoOwner : info) {
                    try {
                        String information = infoOwner.getArrangementPuppy();
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                }
                logger.info("Кнопка 4 работает");
                break;
            case "arrangementDog":
                for (InformationForOwner infoOwner : info) {
                    try {
                        String information = infoOwner.getArrangementDog();
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                }
                logger.info("Кнопка 5 работает");
                break;
            case "arrangementDogInvalid":
                for (InformationForOwner infoOwner : info) {
                    try {
                        String information = infoOwner.getArrangementDogInvalid();
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                }
                logger.info("Кнопка 6 работает");
                break;
            case "cynologist":
                for (InformationForOwner infoOwner : info) {
                    try {
                        String information = infoOwner.getCynologist();
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                }
                logger.info("Кнопка 7 работает");
                break;
            case "goodCynologists":
                for (InformationForOwner infoOwner : info) {
                    try {
                        String information = infoOwner.getGoodCynologists();
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                }
                logger.info("Кнопка 8 работает");
                break;
            case "reject":
                for (InformationForOwner infoOwner : info) {
                    try {
                        String information = infoOwner.getReject();
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                }
                logger.info("Кнопка 9 работает");
                break;
        }
    }

    // public void  savePetPhoto(Update update) throws IOException {
    //    PhotoSize[] photos = update.message().photo();
    //     Long reportId = recordService.findRecordId(update);
    //    petPhotoService.uploadPhoto(reportId, photos );
    //    telegramBot.execute(new SendMessage(update.message().chat().id(), SAVE_INFORMATION.getMessage()));
    //   }
    @Scheduled(cron = "0 0/1 * /1")
    public void checkAvailabilityOfReportAndSendReminder(long chatId, LocalDateTime dateTime) {
        List<Record> records = recordRepository.findAllRecordByChatIdAndDateTime(chatId, dateTime);
        if(records.isEmpty()){
            telegramBot.execute(new SendMessage(chatId, REPORT_REMEMBER.getMessage() + dateTime.toString()));
            logger.info("Пользователю направлено напоминание");
        }
    }

}




