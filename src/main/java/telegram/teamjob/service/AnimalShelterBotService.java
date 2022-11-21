package telegram.teamjob.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.model.Contact;
import telegram.teamjob.model.Shelter;
import telegram.teamjob.repositories.ContactRepository;
import telegram.teamjob.repositories.ShelterRepository;

import javax.annotation.PostConstruct;
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

    /**
     * константа для шаблона контакта
     * @see Contact
     */
    static private final String CONTACT_TEXT_PATTERN = "([0-9\\\\\\\\\\\s]{11})(\\s)([\\W+]+)";
    static private final Pattern pattern = Pattern.compile(CONTACT_TEXT_PATTERN);
    private final Logger logger = LoggerFactory.getLogger(AnimalShelterBotService.class);
    static private String exampleContact = "89061877772 Иванов Иван Иванович";

    public AnimalShelterBotService(TelegramBot telegramBot, ShelterRepository shelterRepository, ContactRepository contactRepository) {
        this.telegramBot = telegramBot;
        this.shelterRepository = shelterRepository;
        this.contactRepository = contactRepository;

    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * метод обрабатывает сообщения/ответы приходящие от пользователя: <br>
     * <b>/start</b> - вызывает метод {@link #checkAnswer(Update)} предоставляющий пользователю основное меню <br>
     * и с его помощью происходит сохранение контакта в базе данных {@link #safeContact(Update)}<br>
     *<b>ответ от пользователя(пользователь нажал одну из кнопок основного меню)</b>
     * вызывает метод {@link #checkButtonAnswer(Update)} <br>
     * который предоставляет пользователю выбранное "подменю"<br>
     * далее метод обрабатывает выбранные пользователем команды "подменю", делая запросы в БД <br>
     * @param updates
     * @return
     */

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            if (update.callbackQuery() == null) {
                logger.info("Processing update: {}", update);
                checkAnswer(update);
            }
            else if(update.callbackQuery()!=null) {
                logger.info("CallBackQuery processing");
                checkButtonAnswer(update);
                {
                    if (update.callbackQuery().data().equals("info") ||
                            update.callbackQuery().data().equals("way") ||
                            update.callbackQuery().data().equals("address") ||
                            update.callbackQuery().data().equals("safety") ||
                            update.callbackQuery().data().equals("volunteer") ||
                            update.callbackQuery().data().equals("workTime")||
                            update.callbackQuery().data().equals("contact") ) {
                        sendAnswer(update);
                    }
                }
            }
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * метод возвращает пользователю приветствие и ссылку на основное меню в виде таблицы <br>
     * @param update
     */

    private void sendGreetingMessage(Update update) {
        String greetingMessage = "Здравствуй @" + update.message().chat().username() + "!\n" +
                "Рады приветствовать Вас в нашем Приюте для Животных\n" +
                "Данный бот поможет вам, если вы задумываетесь о том, чтобы забрать собаку или кошку домой\n" +
                "Выберите пункт меню, который вас интересует";
        telegramBot.execute(new SendMessage(update.message().chat().id(), greetingMessage)
                .replyMarkup(ourMenuButtons()));
        logger.info("Message Sent" + " Method - sendGreeting");
    }
    /**
     * метод обрабатывает сообщение, пришедшее от пользователя: если была введена команда /start  <br>
     * вызывается метод {@link #sendGreetingMessage(Update)} , иначе будет вызван- метод {@link #safeContact(Update)} <br>
     * @param update
     */
    private void checkAnswer(Update update) {
        switch (update.message().text()) {
            case "/start":
                sendGreetingMessage(update);
                logger.info("checkAnswer: case /start");
                break;
            default:
                // telegramBot.execute(new SendMessage(update.message().chat().id(),
                //        "Вы можете обратиться к волонтеру @LnBgrn"));
                safeContact(update);
        }
    }
    /**
     * метод предоставляет пользователю возможность перейти в другие "подменю" (которые будут тоже  в виде таблицы),<br>
     * для этого пользователю необходимо нажать на соотвествующий раздел основного меню  <br>
     * или пользователь может вызвать волонтера <br>
     * @param update
     */
    private void checkButtonAnswer(Update update) {
        String callBackData = update.callbackQuery().data();
        logger.info(callBackData);
        int messageId = update.callbackQuery().message().messageId();
        long chatId = update.callbackQuery().message().chat().id();
        switch (callBackData) {
            case "TEXT1_BUTTON":
                String newMessage = "Вы выбрали раздел " + "\n" + "\"Узнать информацию о приюте\". " + "\n"
                        + "Ознакомьтесь пожалуйста " +
                        "с меню и выберите интересующий вас пункт";
                telegramBot.execute(new SendMessage(chatId, newMessage)
                        .replyMarkup(makeButtonsForMenu()));
                break;

            case "TEXT4_BUTTON":
                //Обращение к волонтеру
                String newMessage4 = "Вы можете обратиться к волонтеру @LnBgrn";
                EditMessageText messageText4 = new EditMessageText(chatId, (int) messageId, newMessage4);
                telegramBot.execute(messageText4);
                break;
        }
    }
    /**
     * кнопки основго меню "Определение запроса"
     */
    private InlineKeyboardMarkup ourMenuButtons() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        String info = "Узнать информацию о приюте";
        String instruction = "Как взять собаку из приюта";
        String record = "Прислать отчет о питомце";
        String help = "Позвать волонтера";

        var button1Info = new InlineKeyboardButton(info);
        var button2Instruction = new InlineKeyboardButton(instruction);
        var button3Record = new InlineKeyboardButton(record);
        var button4Help = new InlineKeyboardButton(help);

        button1Info.callbackData("TEXT1_BUTTON");
        button2Instruction.callbackData("TEXT2_BUTTON");
        button3Record.callbackData("TEXT3_BUTTON");
        button4Help.callbackData("TEXT4_BUTTON");

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
     *  кнопки подменю "Консультация с новым пользователем" (Этап 1)
     */
    private InlineKeyboardMarkup makeButtonsForMenu() {
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

    /**
     * метод обрабатывает ответ поступивший от пользователя, выбравшего соотвествующий <br>
     * <u>раздел подменю:</u> "Консультация с новым пользователем" (Этап 1)<br>
     * метод делает запрос в БД <b>Shelter</b> через вызов {@link ShelterRepository#findAll()}<br>
     * и возвращает пользователю текстовый ответ, с информацией о:<br>
     *  -  приюте <br>
     *  - графике работы приюта<br>
     *  - правилах безопаного поведения при посещении приюта<br>
     *  - том как добраться до приюта<br>
     *  - так же можно вызвать волонтера  - бот отправляет сообщние пользователю, что будет вызван волонтер <br>
     * -  пользователь может оставить для связи свои контактные данные<br>
     * (имя, фамилия, номер телефона) - вызов метода <br>
     * {@link #safeContact(Update)}<br>
     * @throws  NullPointerException если запрашиваемой инормации нет в базе данных
     * @see Shelter
     * @see Contact
     * @param update
     */

    public void sendAnswer(Update update){
        logger.info("вызвал метод sendAnswer");
        String answerMenu = update.callbackQuery().data();
        long chatId = update.callbackQuery().message().chat().id();
        int messageId = update.callbackQuery().message().messageId();
        logger.info("Ответ от кнопки " + answerMenu);
        List<Shelter> shelters = shelterRepository.findAll();
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
                        EditMessageText answer = new EditMessageText(chatId,messageId,workSchedule);
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
                    EditMessageText answer = new EditMessageText(chatId,messageId, address);
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
                        EditMessageText answer = new EditMessageText(chatId, messageId,safety);
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
                telegramBot.execute(new SendMessage(chatId,COMMAND_SAFE_CONTACT_DETAILS_FOR_COMMUNICATION.getText()));
                break;

        }
    }

    /**
     * метод проверяет сообщение от пользователя на соответствие шаблону
     * и преобразует его для внесения в базу данных <br>
     * путем вызова {@link ContactRepository#save(Object)} <br>
     * если сообщение было введено пользователем не верно - бот вернет ответ, <br>
     * что данные не сохранились, так как сообщение не соответсвует шаблону <br>
     * @param update
     */
    public void safeContact(Update update){
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
        }

        else {
            String warningMessage = "Не смогу сохранить запись. Введенное сообщение не соотствует шаблону: "
                    + exampleContact + ". Попробуйте еще раз.";
            telegramBot.execute(new SendMessage(chatId, warningMessage));
            logger.warn("Не смогу сохранить запись. Введенное сообщение не соотствует шаблону");
        }
    }
    /**
     * метод для выполнения {@code GET} запроса в {@code ContactController}<br>
     * найти в БД контак пользователя по его id
     * @see ContactRepository
     * @see Contact
     */
    public Contact findContactById(int id){
        logger.warn("check the correctness of the faculty id");
        return contactRepository.findById(id).orElse(null);
    }
    /**
     * метод для выполнения {@code GET} запроса в {@code ContactController}<br>
     * найти все контакты имеющиеся в БД
     * @see ContactRepository
     * @see Contact
     */
    public Collection<Contact> getAllContacts(){
        return contactRepository.findAll();
    }

    /**
     * метод для выполнения {@code POST} запроса в {@code ContactController}<br>
     * создание нового контакта пользователя в БД
     * @see ContactRepository
     * @see Contact
     */
    public Contact addContact(Contact contact){
        return contactRepository.save(contact);
    }

}
