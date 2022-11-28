package telegram.teamjob.service;

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
import telegram.teamjob.model.Contact;
import telegram.teamjob.model.Shelter;
import telegram.teamjob.repositories.ContactRepository;
import telegram.teamjob.repositories.ShelterRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static telegram.teamjob.model.ServiceConstantsMenu.*;

/**
 * методы сервиса отвечают за: <br>
 * - начало работы с ботом - предоставляет пользователю основное меню/вызвать волонтера <br>
 * - из которого можно перейти в "подменю" для получения нужной информации<br>
 * - сохранение контаных данных о пользователе для обратной связи <br>
 * - из любого "подменю" пользователь имеет возможность вызвать волонтера <br>
 * <i>за вышеуказанный функционал отвечатю:</i><br>
 * <b>сущности:</b>  {@code Shelter, Contact} + две базы данных <br>
 * <b>репозитории</b>: {@code ShelterRepository, ContactRepository}
 */
@Service
public class AnimalShelterBotService implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final ShelterRepository shelterRepository;
    private final ContactRepository contactRepository;
    private final UserService userService;
    private final RecordService recordService;
    private final PetPhotoService petPhotoService;

    /**
     * константа для шаблона контакта
     *
     * @see Contact
     */
    static private final String CONTACT_TEXT_PATTERN = "([0-9\\\\\\\\\\\s]{11})(\\s)([\\W+]+)";

    static private boolean flag;
    static private final Pattern pattern = Pattern.compile(CONTACT_TEXT_PATTERN);
    private final Logger logger = LoggerFactory.getLogger(AnimalShelterBotService.class);
    static private String exampleContact = "89061877772 Иванов Иван Иванович";

    public AnimalShelterBotService(TelegramBot telegramBot, ShelterRepository shelterRepository, ContactRepository contactRepository, UserService userService, RecordService recordService, PetPhotoService petPhotoService) {
        this.telegramBot = telegramBot;
        this.shelterRepository = shelterRepository;
        this.contactRepository = contactRepository;
        this.userService = userService;
        this.recordService = recordService;
        this.petPhotoService = petPhotoService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * метод обрабатывает сообщения/ответы приходящие от пользователя: <br>
     * <b>/start</b> - вызывает метод {@link #sendAnswer(Update)} предоставляющий пользователю основное меню <br>
     * и с его помощью происходит сохранение контакта в базе данных {@link #safeContact(Update)}<br>
     * <b>ответ от пользователя(пользователь нажал одну из кнопок основного меню)</b>
     * вызывает метод {@link #checkButtonAnswer(Update)} <br>
     * который предоставляет пользователю выбранное "подменю"<br>
     * далее метод обрабатывает выбранные пользователем команды "подменю", делая запросы в БД <br>
     *
     * @param updates
     * @return
     */

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            try {
                messageHandler(update);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void messageHandler(Update update) {
        if (update.callbackQuery() != null) {
            logger.info("CallBackQuery processing");
            checkButtonAnswer(update);

            if (checkCallBackQuery(update)) {
                sendAnswer(update);
            }

        } else if (update.message().photo() != null) {
            logger.info("Photo Upload processing");
            long recordId = recordService.findRecordId(update);
            PhotoSize[] photos = update.message().photo();
            try {
                petPhotoService.uploadPhoto(recordId, photos);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Кнопки меню -> Нужен рефактор
    private boolean checkCallBackQuery(Update update) {
        if (update.callbackQuery().data().equals("info") ||
                update.callbackQuery().data().equals("way") ||
                update.callbackQuery().data().equals("address") ||
                update.callbackQuery().data().equals("safety") ||
                update.callbackQuery().data().equals("volunteer") ||
                update.callbackQuery().data().equals("workTime") ||
                update.callbackQuery().data().equals("contact") ||
                update.callbackQuery().data().equals("rules") ||
                update.callbackQuery().data().equals("transportation)") ||
                update.callbackQuery().data().equals("arrangementPuppy") ||
                update.callbackQuery().data().equals("arrangementDog") ||
                update.callbackQuery().data().equals("arrangementDogInvalid") ||
                update.callbackQuery().data().equals("cynologist") ||
                update.callbackQuery().data().equals("reject") ||
                update.callbackQuery().data().equals("contact") ||
                update.callbackQuery().data().equals("help")) {
            return true;
        } else {
            return false;
        }
    }

    private SendMessage sendMessage(long chatId, String textToSend) {
        return new SendMessage(chatId, textToSend);
    }

    /**
     * метод возвращает пользователю приветствие и ссылку на основное меню в виде таблицы <br>
     *
     * @param update
     */

    private void sendGreetingMessage(Update update) {
        String greetingMessage = BotMessageEnum.START_MESSAGE.getMessage();
        telegramBot.execute(sendMessage(update.message().chat().id(), greetingMessage)
                .replyMarkup(ourMenuButtons()));
        logger.info("Message Sent" + " Method - sendGreeting");
    }

    /**
     * метод обрабатывает сообщение, пришедшее от пользователя: если была введена команда /start  <br>
     * вызывается метод {@link #sendGreetingMessage(Update)} , иначе будет вызван- метод {@link #safeContact(Update)} <br>
     *
     * @param update
     */
//    private void checkAnswer(Update update) {
//        switch (update.message().text()) {
//            case "/start":
//                sendGreetingMessage(update);
//                logger.info("checkAnswer: case /start");
//                break;
//            default:
//                // telegramBot.execute(new SendMessage(update.message().chat().id(),
//                //        "Вы можете обратиться к волонтеру @LnBgrn"));
//                safeContact(update);
//        }
//    }

    /**
     * метод предоставляет пользователю возможность перейти в другие "подменю" (которые будут тоже  в виде таблицы),<br>
     * для этого пользователю необходимо нажать на соотвествующий раздел основного меню  <br>
     * или пользователь может вызвать волонтера <br>
     *
     * @param update
     */
    private void checkButtonAnswer(Update update) {
        String callBackData = update.callbackQuery().data();
        logger.info(callBackData);
        long messageId = update.callbackQuery().message().messageId();
        long chatId = update.callbackQuery().message().chat().id();
        switch (callBackData) {
            case "Узнать информацию о приюте":
                // Заглушка. Необходимо поменять на вызов метода получения информации (Этап1 п2)
                String newMessage = "Кнопка 1 работает";
                EditMessageText messageText = new EditMessageText(chatId, (int) messageId, newMessage);
                telegramBot.execute(messageText);
                break;
            case "Как взять собаку из приюта":
                // Необходимо заменить на вывод инструкции
                String newMessage2 = "Кнопка 2 работает";
                EditMessageText messageText2 = new EditMessageText(chatId, (int) messageId, newMessage2);
                telegramBot.execute(messageText2);
                break;
            case "Прислать отчет о питомце":
                //Отчет по питомцу
                String messageForRecords = BotMessageEnum.DAILY_RECORD_INFO.getMessage();
                EditMessageText messageText3 = new EditMessageText(chatId, (int) messageId, messageForRecords);
                telegramBot.execute(messageText3);
                flag = true;
                break;
            case "Позвать волонтера":
                //Обращение к волонтеру
                String newMessage4 = BotMessageEnum.ASK_HELP.getMessage();
                EditMessageText messageText4 = new EditMessageText(chatId, (int) messageId, newMessage4);
                telegramBot.execute(messageText4);
                break;

                /*
            case "TEXT21_BUTTON":
                // Правила
                String newMessage21 = "Кнопка 1 работает";
                EditMessageText messageText = new EditMessageText(chatId,
                        (int) messageId, newMessage);
                telegramBot.execute(messageText);
                break;
            case "TEXT22_BUTTON":
                // Необходимо заменить на вывод инструкции
                String newMessage22 = "Кнопка 2 работает";
                EditMessageText messageText2 = new EditMessageText(chatId,
                        (int) messageId, newMessage2);
                telegramBot.execute(messageText2);
                break;
            case "TEXT3_BUTTON":
                //Отчет по питомцу
                String newMessage23 = "Кнопка 3 работает";
                EditMessageText messageText3 = new EditMessageText(chatId,
                        (int) messageId, newMessage3);
                telegramBot.execute(messageText3);
                break;
            case "TEXT4_BUTTON":
                //Обращение к волонтеру
                String newMessage24 = "Вы можете обратиться к волонтеру @RRRNikita";
                EditMessageText messageText4 = new EditMessageText(chatId,
                            (int) messageId, newMessage4);
                telegramBot.execute(messageText4);
                break;
            case "TEXT5_BUTTON":
                // Рекомендации щенку
                String newMessage25 = "Кнопка 5 работает";
                EditMessageText messageText5 = new EditMessageText(chatId,
                        (int) messageId, newMessage5);
                telegramBot.execute(messageText5);
                break;
            case "TEXT6_BUTTON":
                // рекомендации взрослой собаке
                String newMessage26 = "Кнопка 6 работает";
                EditMessageText messageText6 = new EditMessageText(chatId,
                        (int) messageId, newMessage6);
                telegramBot.execute(messageText6);
                break;
            case "TEXT7_BUTTON":
                // кинолог
                String newMessage27 = "Кнопка 7 работает";
                EditMessageText messageText7 = new EditMessageText(chatId,
                        (int) messageId, newMessage7);
                telegramBot.execute(messageText7);
                break;
            case "TEXT8_BUTTON":
                // контакты
                String newMessage28 = "Кнопка 8 работает";
                EditMessageText messageText8 = new EditMessageText(chatId,
                        (int) messageId, newMessage8);
                telegramBot.execute(messageText8);
                break;

            case "TEXT9_BUTTON":
                // возможнные причины для отказа
                String newMessage29 = "Кнопка 9 работает";
                EditMessageText messageText9 = new EditMessageText(chatId,
                        (int) messageId, newMessage9);
                telegramBot.execute(messageText9);
                break;

            case "TEXT10_BUTTON":
                //Обращение к волонтеру
                String newMessage20 = "Кнопка 10 работает";
                EditMessageText messageText10 = new EditMessageText(chatId,
                        (int) messageId, newMessage10);
                telegramBot.execute(messageText10);
                break;

                 */
        }
    }

    /**
     * кнопки основго меню "Определение запроса"
     * Метод добавляет под сообщение кнопки с меню.
     * Клиент может выбрать интересующий его пункт и получить информацию
     */
    private InlineKeyboardMarkup ourMenuButtons() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        var button1Info = new InlineKeyboardButton(BotButtonEnum.BUTTON_INFO.getMessage());
        var button2Instruction = new InlineKeyboardButton(BotButtonEnum.BUTTON_INSTRUCTION.getMessage());
        var button3Record = new InlineKeyboardButton(BotButtonEnum.BUTTON_RECORD.getMessage());
        var button4Help = new InlineKeyboardButton(BotButtonEnum.BUTTON_HELP.getMessage());

        button1Info.callbackData(BotButtonEnum.BUTTON_INFO.getMessage());
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
        return markup;
    }

    /**
     * кнопки подменю "Консультация с новым пользователем" (Этап 1)
     */
    private InlineKeyboardMarkup makeButtonsForMenu() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        var buttonAboutShelter = new com.pengrad.telegrambot.model.request.InlineKeyboardButton(COMMAND_INFORMATION_ABOUT_SHELTER.getText());
        var buttonWorkTime = new com.pengrad.telegrambot.model.request.InlineKeyboardButton(COMMAND_WORK_SCHEDULE_SHELTER.getText());
        var buttonAddress = new com.pengrad.telegrambot.model.request.InlineKeyboardButton(COMMAND_ADDRESS_SHELTER.getText());
        var buttonWay = new com.pengrad.telegrambot.model.request.InlineKeyboardButton(COMMAND_DRIVING_DIRECTIONS.getText());
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

    /*
    private InlineKeyboardMarkup secondMenuButtons() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        String rules = " Правила знакомства с животным ";
        String docs = " Список необходимых документов ";
        String transportation = " Рекомендация по транспортировке ";
        String arrangementPuppy = " Рекомендации по устройству дома для щенка ";
        String arrangementDog = " Рекомендации по устройству дома для взрослой собаки ";
        String arrangementDogInvalid = " Рекомендации по устройству дома для собаки с " +
                " ограниченными возможностями ";
        String cynologist= " Рекомендации по общению с собакой от кинологов";
        String reject = " Спиоск причин отказа ";
        String contact = " Контактные данные ";
        String help = " Позвать волонтера ";

        var button1Rules = new InlineKeyboardButton(rules);
        var button2Docs = new InlineKeyboardButton(docs);
        var button3Transportation = new InlineKeyboardButton(transportation);
        var button4ArrangementPuppy = new InlineKeyboardButton(arrangementPuppy);
        var button5ArrangementDog = new InlineKeyboardButton(arrangementDog);
        var button6ArrangementDogInvalid = new InlineKeyboardButton(arrangementDogInvalid);
        var button7Cynologist = new InlineKeyboardButton(cynologist);
        var button8Reject = new InlineKeyboardButton(reject);
        var button9Contact = new InlineKeyboardButton(contact);
        var button10Help = new InlineKeyboardButton(help);

        button1Rules.callbackData("TEXT2.1_BUTTON");
        button2Docs.callbackData("TEXT2.2_BUTTON");
        button3Transportation.callbackData("TEXT2.3_BUTTON");
        button4ArrangementPuppy.callbackData("TEXT2.4_BUTTON");
        button5ArrangementDog.callbackData("TEXT2.5_BUTTON");
        button6ArrangementDogInvalid.callbackData("TEXT2.6_BUTTON");
        button7Cynologist.callbackData("TEXT2.7_BUTTON");
        button8Reject.callbackData("TEXT2.8_BUTTON");
        button9Contact.callbackData("TEXT2.9_BUTTON");
        button10Help.callbackData("TEXT2.10_BUTTON");

        markup.addRow(button1Rules);
        markup.addRow(button2Docs);
        markup.addRow(button3Transportation);
        markup.addRow(button4ArrangementPuppy);
        markup.addRow(button5ArrangementDog);
        markup.addRow(button6ArrangementDogInvalid);
        markup.addRow(button7Cynologist);
        markup.addRow(button8Reject);
        markup.addRow(button9Contact);
        markup.addRow(button10Help);
        return markup;
    }
    */

    /**
     * метод обрабатывает ответ поступивший от пользователя, выбравшего соотвествующий <br>
     * <u>раздел подменю:</u> "Консультация с новым пользователем" (Этап 1)<br>
     * метод делает запрос в БД <b>Shelter</b> через вызов {@link ShelterRepository#findAll()}<br>
     * и возвращает пользователю текстовый ответ, с информацией о:<br>
     * -  приюте <br>
     * - графике работы приюта<br>
     * - правилах безопаного поведения при посещении приюта<br>
     * - том как добраться до приюта<br>
     * - так же можно вызвать волонтера  - бот отправляет сообщние пользователю, что будет вызван волонтер <br>
     * -  пользователь может оставить для связи свои контактные данные<br>
     * (имя, фамилия, номер телефона) - вызов метода <br>
     * {@link #safeContact(Update)}<br>
     *
     * @param update
     * @throws NullPointerException если запрашиваемой инормации нет в базе данных
     * @see Shelter
     * @see Contact
     */

    public void sendAnswer(Update update) {
        logger.info("вызвал метод sendAnswer");
        String answerMenu = update.callbackQuery().data();
        long chatId = update.callbackQuery().message().chat().id();
        int messageId = update.callbackQuery().message().messageId();
        logger.info("Ответ от кнопки " + answerMenu);
        List<Shelter> shelters = shelterRepository.findAll();
        if (answerMenu != null) {
            if (answerMenu.equals("info")) {
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
            } else if (answerMenu.equals("/start")) {
                sendGreetingMessage(update);
            } else if (answerMenu.equals("/createuser")) {
                telegramBot.execute(sendMessage(chatId, BotMessageEnum.CREATE_USER_INFO.getMessage()));
            } else if (answerMenu.matches(CONTACT_TEXT_PATTERN)) {
                // Если текст подходит по паттерн - Создаём юзера
                logger.info("Processing checkAnswer:");
                // Кать, здесь мой метод, видел твой внизу, посмотри что лучше подойдёт (Мой вряд ли под паттерн сойдёт)
                userService.createUser(update);
            } else if (answerMenu.equals("/send-recod")) {
                //Отправляет юзеру шаблон отчета
                String messageForRecords = BotMessageEnum.DAILY_RECORD_INFO.getMessage();
                telegramBot.execute(sendMessage(chatId, messageForRecords));
            } else if (flag) {
                //При нажатии на кнопку 3 (Отправить отчет) flag = true;
                logger.info("Processing creating record");
                // Нужен if На случай некорректного сообщения
                if (userService.getUser(chatId) != null) {
                    recordService.createRecord(update);
                } else {
                    telegramBot.execute(sendMessage(chatId, BotMessageEnum.USER_NOT_FOUND_MESSAGE.getMessage()));
                }
                flag = false;
            } else if (answerMenu.equals("workTime")) {
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
            } else if (answerMenu.equals("address")) {
                for (Shelter shelter : shelters) {
                    String address = shelter.getAddressShelter();
                    EditMessageText answer = new EditMessageText(chatId, messageId, address);
                    logger.warn("IMPORTANT" + address);
                    telegramBot.execute(answer);
                }
            } else if (answerMenu.equals("way")) {
                for (Shelter shelter : shelters) {
                    try {
                        String way = shelter.getDrivingDirectionsShelter();
                        EditMessageText answer = new EditMessageText(chatId, messageId, way);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");

                    }
                }
            } else if (answerMenu.equals("safety")) {
                for (Shelter shelter : shelters) {
                    try {
                        String safety = shelter.getSafetyAtShelter();
                        EditMessageText answer = new EditMessageText(chatId, messageId, safety);
                        telegramBot.execute(answer);
                    } catch (NullPointerException e) {
                        logger.warn("Инфopмации в базе данных нет");

                    }
                }
            } else if (answerMenu.equals("volunteer")) {
                telegramBot.execute(new SendMessage(chatId, "Спасибо за обращение, волонтер приюта свяжется " +
                        "с Вами"));
            } else if (answerMenu.equals("contact")) {
                telegramBot.execute(new SendMessage(chatId, COMMAND_SAFE_CONTACT_DETAILS_FOR_COMMUNICATION.getText()));
            } else {
                telegramBot.execute(sendMessage(chatId, BotMessageEnum.ASK_HELP.getMessage()));
            }
        } else {
            telegramBot.execute(sendMessage(chatId, BotMessageEnum.ASK_HELP.getMessage()));
        }
    }

    /**
     * метод проверяет сообщение от пользователя на соответствие шаблону
     * и преобразует его для внесения в базу данных <br>
     * путем вызова {@link ContactRepository#save(Object)} <br>
     * если сообщение было введено пользователем не верно - бот вернет ответ, <br>
     * что данные не сохранились, так как сообщение не соответсвует шаблону <br>
     *
     * @param update
     */
    public void safeContact(Update update) {
        String text = update.message().text();
        long chatId = update.message().chat().id();
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            String phone = matcher.group(1);
            String name = matcher.group(3);
            Contact contact = new Contact();
            contact.setNumberPhone(phone);
            contact.setName(name);
            contactRepository.save(contact);
            String messageForBase = "Я сохранил ваши данные в базе данных. Сотрудники приюта свяжутся с Вами.";
            telegramBot.execute(new SendMessage(chatId, messageForBase));
            logger.info("Данные сохранены в базе данных");
        } else {
            String warningMessage = "Не смогу сохранить запись. Введенное сообщение не соотствует шаблону: "
                    + exampleContact + ". Попробуйте еще раз.";
            telegramBot.execute(new SendMessage(chatId, warningMessage));
            logger.warn("Не смогу сохранить запись. Введенное сообщение не соотствует шаблону");
        }
    }

    /**
     * метод для выполнения {@code GET} запроса в {@code ContactController}<br>
     * найти в БД контак пользователя по его id
     *
     * @see ContactRepository
     * @see Contact
     */
    public Contact findContactById(int id) {
        logger.warn("check the correctness of the faculty id");
        return contactRepository.findById(id).orElse(null);
    }

    /**
     * метод для выполнения {@code GET} запроса в {@code ContactController}<br>
     * найти все контакты имеющиеся в БД
     *
     * @see ContactRepository
     * @see Contact
     */
    public Collection<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    /**
     * метод для выполнения {@code POST} запроса в {@code ContactController}<br>
     * создание нового контакта пользователя в БД
     *
     * @see ContactRepository
     * @see Contact
     */
    public Contact addContact(Contact contact) {
        return contactRepository.save(contact);
    }

}
