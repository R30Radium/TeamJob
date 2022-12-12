package telegram.teamjob.Service;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.constants.BotButtonEnum;
import telegram.teamjob.constants.BotMessageEnum;
import telegram.teamjob.entity.Contact;
import telegram.teamjob.entity.InformationForOwner;
import telegram.teamjob.entity.Shelter;
import telegram.teamjob.entity.User;
import telegram.teamjob.repositories.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static telegram.teamjob.constants.BotButtonEnum.*;
import static telegram.teamjob.constants.BotMessageEnum.*;
import static telegram.teamjob.constants.ServiceConstantsMenu.*;

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

    private final TelegramBot telegramBot;
    private final ShelterRepository shelterRepository;
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final InformationForOwnerRepository informationForOwnerRepository;
    private final RecordServiceImpl recordService;

    private final PetPhotoServiceImpl petPhotoService;

    /**
     * константа для шаблона
     * @see Contact
     */
    static public final String CONTACT_TEXT_PATTERN = "([0-9]{11})(\\s)([\\W+]+)";
    static public final Pattern PATTERN = Pattern.compile(CONTACT_TEXT_PATTERN);
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    static private String exampleContact = "89061877772 Иванов Иван Иванович";
    private boolean flag;


    public TelegramBotUpdatesListener(TelegramBot telegramBot, ShelterRepository shelterRepository,
                                      ContactRepository contactRepository,
                                      UserRepository userRepository, InformationForOwnerRepository informationForOwnerRepository,
                                      RecordServiceImpl recordService,
                                      PetPhotoServiceImpl petPhotoService) {
        this.telegramBot = telegramBot;
        this.shelterRepository = shelterRepository;
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.informationForOwnerRepository = informationForOwnerRepository;
        this.recordService = recordService;
        this.petPhotoService = petPhotoService;
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
                throw new RuntimeException("ERROR" + e);
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
    public void messageHandler(Update update) {
         if (update.callbackQuery() != null) {
            System.out.println(update);
            logger.info("CallBackQuery processing");
            checkButtonAnswer(update);
            if (update.callbackQuery().data().equals("info") || //команды для меню первого этапа
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
                    update.callbackQuery().data().equals("transportation") ||
                    update.callbackQuery().data().equals("arrangementPuppy") ||
                    update.callbackQuery().data().equals("arrangementDog") ||
                    update.callbackQuery().data().equals("arrangementDogInvalid")||
                    update.callbackQuery().data().equals("cynologist") ||
                    update.callbackQuery().data().equals("goodCynologists") ||
                    update.callbackQuery().data().equals("reject")){
                sendResponseForFirstAndSecondMenu(update);
            }
        }
        else if (update.message().photo() != null) {
            logger.info("Photo Upload processing");
            Long recordId = recordService.findRecordId(update);
            PhotoSize[] photos = update.message().photo();
            if (recordId != null) {
                try {
                    petPhotoService.uploadPhoto(recordId, photos);
                } catch (IOException e) {
                    logger.info("Error" + e);
                    telegramBot.execute(sendMessage(update.message().chat().id(), BotMessageEnum.ASK_HELP.getMessage()));
                }
            } else {
                telegramBot.execute(sendMessage(update.message().chat().id(), "Сперва Отчет\n" +
                        BotMessageEnum.DAILY_RECORD_INFO));
            }
        } else {
            logger.info("Processing update: {}", update);
            checkAnswer(update);
        }
    }

    public void checkAnswer(Update update) {
        Long chatId = update.message().chat().id();
        String inputText = update.message().text();
        if (inputText != null) {
            if (inputText.equals("/start")) {
                sendGreetingMessage(update);
            } else if (inputText.equals("/createuser")) {
                telegramBot.execute(sendMessage(chatId, BotMessageEnum.CREATE_USER_INFO.getMessage()));
                //  } else if (inputText.matches("([A-zА-я]+)(\\s)([0-9]+)(\\s)([A-zА-я]+)")) {
            } else if (inputText.matches(CONTACT_TEXT_PATTERN)) {
                logger.info("Processing checkAnswer:");
                // userService.createUser(update);
                saveContact(update);
                logger.info("save contact");
            } else if (inputText.equals("/send-record")) {
                String messageForRecords = BotMessageEnum.DAILY_RECORD_INFO.getMessage();
                telegramBot.execute(sendMessage(chatId, messageForRecords));
            } else if (flag) {
                //При нажатии на кнопку 3 (Отправить отчет)
                logger.info("Processing creating record");
                // Нужен if На случай некорректного сообщения
                if (userRepository.findByChatId(chatId) != null) {
                    recordService.createRecord(update);
                } else {
                    telegramBot.execute(sendMessage(chatId, BotMessageEnum.USER_NOT_FOUND_MESSAGE.getMessage()));
                }
                flag = false;
            } else {
                telegramBot.execute(sendMessage(chatId, BotMessageEnum.ASK_HELP.getMessage()));
            }
        } else {
            telegramBot.execute(sendMessage(chatId, BotMessageEnum.ASK_HELP.getMessage()));
        }
    }



    /**
     * метод возвращает пользователю приветственное сообшение и <br>
     * кнопки основного меню <br>
     */

    public void sendGreetingMessage(Update update) {
        try {
            String greetingMessage = "@"+update.message().chat().username() + BotMessageEnum.START_MESSAGE.getMessage();
            telegramBot.execute(new SendMessage(update.message().chat().id(), greetingMessage)
                    .replyMarkup(ourMenuButtons()));
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
        int messageId = update.callbackQuery().message().messageId();
        long chatId = update.callbackQuery().message().chat().id();
        switch (callBackData) {
            case "Узнать информацию о приюте":
                //вызов  меню этапа 1
                String newMessage = USER_CHOOSE_SHELTER_INFO.getMessage();
                //  EditMessageText messageText = new EditMessageText(chatId, messageId, newMessage);
                telegramBot.execute(new SendMessage(chatId, newMessage)
                        .replyMarkup(makeButtonsForMenuStageOne()));
                break;
            case "Как взять собаку из приюта":
                //вызов  меню этапа 2
                String message = USER_CHOOSE_DOG_INSTRUCTION.getMessage();
                telegramBot.execute(new SendMessage(chatId, message)
                        .replyMarkup(makeButtonsForMenuStageTwo()));
                break;
            case "Прислать отчет о питомце":
                //Отчет по питомцу
                String creatRecordMessage = BotMessageEnum.DAILY_RECORD_INFO.getMessage();
                telegramBot.execute(new SendMessage(chatId, creatRecordMessage));
                logger.info("Message Sent" + " Method - sendRecord");
                flag = true;
                break;
            case "Позвать волонтера":
                //Обращение к волонтеру
                String newMessage4 = BotMessageEnum.ASK_HELP.getMessage();
                EditMessageText messageText4 = new EditMessageText(chatId, (int) messageId, newMessage4);
                telegramBot.execute(messageText4);
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
                logger.info("case");
                    try {
                        logger.info("try case 1");
//                        String information = shelter.getInformationAboutShelter();
                        String information = "Информация о приюте";
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                break;
            case "workTime":
                    try {
//                        String workSchedule = shelter.getWorkScheduleShelter();
                        String workSchedule = "24/7 без перерывов";
                        EditMessageText answer = new EditMessageText(chatId, messageId, workSchedule);
                        logger.warn("IMPORTANT" + workSchedule);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                break;
            case "address":
                try {
//                    String address = shelter.getAddressShelter();
                    String address = "Улица чертовых тестов";
                    EditMessageText answer = new EditMessageText(chatId, messageId, address);
                    logger.warn("IMPORTANT" + address);
                    telegramBot.execute(answer);
                } catch (Exception e) {
                    logger.warn("Ошибка в кейсе address: " + e);
                }
                break;
            case "way":
                    try {
//                        String way = shelter.getDrivingDirectionsShelter();
                        String way = "Путь через боль";
                        EditMessageText answer = new EditMessageText(chatId, messageId, way);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                break;
            case "safety":
                    try {
//                        String safety = shelter.getSafetyAtShelter();
                        String safety = "тестовый ответ";
                        EditMessageText answer = new EditMessageText(chatId, messageId, safety);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");

                    }
                break;
            case "volunteer":
                telegramBot.execute(new EditMessageText(chatId, messageId,"Спасибо за обращение, волонтер приюта свяжется " +
                        "с Вами"));
                break;
            case "contact":
                telegramBot.execute(new EditMessageText(chatId, messageId, COMMAND_SAFE_CONTACT_DETAILS_FOR_COMMUNICATION.getText()));
                break;
            case "rules":
                    try {
//                        String information = infoOwner.getRules();
                        String information = "Забери 2 собаки";
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                logger.info("Кнопка 1 работает");
                break;
            case "docs":
                    try {
                        String information = "Документы";
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                logger.info("Кнопка 2 работает");
                break;
            case "transportation":
                    try {
//                        String information = infoOwner.getTranspartation();
                        String information = "Перевозка";
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                logger.info("Кнопка 3 работает");
                break;
            case "arrangementPuppy":
                    try {
                        String information = "Щеночки";
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                logger.info("Кнопка 4 работает");
                break;
            case "arrangementDog":
                    try {
                        String information = "Собачки";
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                logger.info("Кнопка 5 работает");
                break;
            case "arrangementDogInvalid":
                    try {
                        String information = "Собачки 2";
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                logger.info("Кнопка 6 работает");
                break;
            case "cynologist":
                    try {
                        String information = "Тест";
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                logger.info("Кнопка 7 работает");
                break;
            case "goodCynologists":
                    try {
                        String information = "Тест 2";
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                logger.info("Кнопка 8 работает");
                break;
            case "reject":
                    try {
                        String information = "Тест3";
                        EditMessageText answer = new EditMessageText(chatId, messageId, information);
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");
                    }
                logger.info("Кнопка 9 работает");
                break;
        }
    }

    /**
     * метод проверяет сообщение от пользователя на соответствие шаблону и преобразует его для внесения
     * в БД  путем вызова метода репозитория {@link ContactRepository#save(Object)} <br>
     * если сообщение было введено пользователем не верно - бот вернет ответ - что данные не сохранились, так
     * как сообщение не соответсвует шаблону
     * так же метод проверяет есть ли уже такой контакт в БД путем вызова метода репозитория <br>
     * {@link ContactRepository#findContactByNumberPhoneAndName(String, String)} , <br>
     * если контакт ранее уже был внесен в БД - бот сообщает об этом пользователю  <br>
     * @param update
     */
    public void saveContact(Update update){
        String text = update.message().text();
        long chatId = update.message().chat().id();
        Matcher matcher = PATTERN.matcher(text);
        if (matcher.matches()) {
            String phone = matcher.group(1);
            String name = matcher.group(3);
            Contact contact = new Contact();
            contact.setNumberPhone(phone);
            contact.setName(name);

            User user = new User();
            user.setUserName(name);
            user.setNumberPhone(phone);
            user.setChatId(chatId);
            List<Contact> contacts = contactRepository.findContactByNumberPhoneAndName(phone,name);
            List<User> users = userRepository.findAll();
            logger.info(String.valueOf(contacts));
            try{
                if(!contacts.contains(contact) || !users.contains(user)){
                    contactRepository.save(contact);
                    userRepository.save(user);
                    telegramBot.execute(new SendMessage(chatId, "Я сохранил ваши данные в базе данных." +
                            " Сотрудники приюта свяжутся с Вами."));
                    logger.info("Данные сохранены в базе данных");
                } else {
                    logger.info("такой контакт уже есть в базе данных");
                    telegramBot.execute(new SendMessage(chatId, "Такой контакт уже есть в базе данных"));
                }
            }
            catch(NullPointerException e){
                logger.info("пользователь предоставил новый контакт");
            }
        }
        else {
            String warningMessage = "Не смогу сохранить запись. Введенное сообщение не соотствует шаблону: "
                    + exampleContact + ". Попробуйте еще раз.";
            telegramBot.execute(new SendMessage(chatId, warningMessage));
            logger.warn("Не смогу сохранить запись. Введенное сообщение не соотствует шаблону");
        }

    }

    private InlineKeyboardMarkup ourMenuButtons() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        var button1Info = new InlineKeyboardButton(BUTTON_INFO.getMessage());
        var button2Instruction = new InlineKeyboardButton(BotButtonEnum.BUTTON_INSTRUCTION.getMessage());
        var button3Record = new InlineKeyboardButton(BotButtonEnum.BUTTON_RECORD.getMessage());
        var button4Help = new InlineKeyboardButton(BotButtonEnum.BUTTON_HELP.getMessage());

        button1Info.callbackData(BUTTON_INFO.getMessage());
        button2Instruction.callbackData(BotButtonEnum.BUTTON_INSTRUCTION.getMessage());
        button3Record.callbackData(BotButtonEnum.BUTTON_RECORD.getMessage());
        button4Help.callbackData(BotButtonEnum.BUTTON_HELP.getMessage());

        /* Данные кнопки будут располагаться друг под другом. Можно использоваться в линию
           для этого необходимые кнопки можно перечислить в параметрах markup.addRow(b1, b2, b3, b4);
           Тогда информация будет сжата по размеру кнопки
        */
        markup.addRow(button1Info);
        markup.addRow(button2Instruction);
        markup.addRow(button3Record);
        markup.addRow(button4Help);
        logger.info("Меню отправлено");
        return markup;
    }
    private InlineKeyboardMarkup makeButtonsForMenuStageOne() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        var buttonAboutShelter  = new com.pengrad.telegrambot.model.request.InlineKeyboardButton(COMMAND_INFORMATION_ABOUT_SHELTER.getText());
        var  buttonWorkTime = new com.pengrad.telegrambot.model.request.InlineKeyboardButton(COMMAND_WORK_SCHEDULE_SHELTER.getText());
        var  buttonAddress = new com.pengrad.telegrambot.model.request.InlineKeyboardButton(COMMAND_ADDRESS_SHELTER.getText());
        var   buttonWay = new com.pengrad.telegrambot.model.request.InlineKeyboardButton(COMMAND_DRIVING_DIRECTIONS.getText());
        var buttonSafety = new com.pengrad.telegrambot.model.request.InlineKeyboardButton(COMMAND_SAFETY_SHELTER.getText());
        var buttonContact = new com.pengrad.telegrambot.model.request.InlineKeyboardButton("Оставить данные для связи");
        var buttonVolunteer = new com.pengrad.telegrambot.model.request.InlineKeyboardButton(COMMAND_CALL_VOLUNTEER.getText());

        buttonAboutShelter.callbackData("info");
        buttonWorkTime.callbackData("workTime");
        buttonAddress.callbackData("address");
        buttonWay.callbackData("way");
        buttonSafety.callbackData("safety");
        buttonContact.callbackData("contact");
        buttonVolunteer.callbackData("volunteer");

        markup.addRow(buttonAboutShelter);
        markup.addRow(buttonWorkTime);
        markup.addRow(buttonAddress);
        markup.addRow(buttonWay);
        markup.addRow(buttonSafety);
        markup.addRow(buttonContact);
        markup.addRow(buttonVolunteer);
        return markup;
    }
    private InlineKeyboardMarkup makeButtonsForMenuStageTwo() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        String rules = " Правила знакомства с животным ";
        String docs = " Список необходимых документов ";
        String transportation = " Рекомендация по транспортировке ";
        String arrangementPuppy = " Как подготовить дом для щенка ";
        String arrangementDog = " Как подготовить дом для взрослой собаки ";
        String arrangementDogInvalid = " Как подготовить дом для собаки с " +
                " ограниченными возможностями ";
        String cynologist= " Рекомендации от кинологов";
        String goodCynologist = " Хорошие кинологи ";
        String reject = " Спиоск причин отказа ";
        String contact = " Контактные данные ";
        String volunteer = " Позвать волонтера ";

        var button1Rules = new InlineKeyboardButton(rules);
        var button2Docs = new InlineKeyboardButton(docs);
        var button3Transportation = new InlineKeyboardButton(transportation);
        var button4ArrangementPuppy = new InlineKeyboardButton(arrangementPuppy);
        var button5ArrangementDog = new InlineKeyboardButton(arrangementDog);
        var button6ArrangementDogInvalid = new InlineKeyboardButton(arrangementDogInvalid);
        var button7Cynologist = new InlineKeyboardButton(cynologist);
        var button8GoodCynologist = new InlineKeyboardButton(goodCynologist);
        var button9Reject = new InlineKeyboardButton(reject);
        var button10Contact = new InlineKeyboardButton(contact);
        var button11Volunteer = new InlineKeyboardButton(volunteer);

        button1Rules.callbackData("rules");
        button2Docs.callbackData("docs");
        button3Transportation.callbackData("transpartation");
        button4ArrangementPuppy.callbackData("arrangementPuppy");
        button5ArrangementDog.callbackData("arrangementDog");
        button6ArrangementDogInvalid.callbackData("arrangementDogInvalid");
        button7Cynologist.callbackData("cynologist");
        button8GoodCynologist.callbackData("goodCynologists");
        button9Reject.callbackData("reject");
        button10Contact.callbackData("contact");
        button11Volunteer.callbackData("volunteer");

        markup.addRow(button1Rules);
        markup.addRow(button2Docs);
        markup.addRow(button3Transportation);
        markup.addRow(button4ArrangementPuppy);
        markup.addRow(button5ArrangementDog);
        markup.addRow(button6ArrangementDogInvalid);
        markup.addRow(button7Cynologist);
        markup.addRow(button8GoodCynologist);
        markup.addRow(button9Reject);
        markup.addRow(button10Contact);
        markup.addRow(button11Volunteer);
        return markup;
    }

    public Optional<Contact> findContactById(int id){
        logger.warn("check the correctness of the faculty id");
        return contactRepository.findById(id);
    }

    public List<Contact> getAllContacts(){
        return contactRepository.findAll();
    }
    public Contact addContact(Contact contact){
        return contactRepository.save(contact);
    }

}

