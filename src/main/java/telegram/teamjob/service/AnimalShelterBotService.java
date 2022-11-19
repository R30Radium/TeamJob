package telegram.teamjob.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
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
public class AnimalShelterBotService implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final ShelterRepository shelterRepository;
    private final ContactRepository contactRepository;

    /**
     * константа для шаблона
     * @see Contact
     */
    static private final String CONTACT_TEXT_PATTERN = "([0-9\\\\\\\\\\\s]{11})(\\s)([\\W+]+)";
    static private final Pattern pattern = Pattern.compile(CONTACT_TEXT_PATTERN);
    private final Logger logger = LoggerFactory.getLogger(AnimalShelterBotService.class);
    static private String contact = "89061877772 Иванов Иван Иванович";

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
     * метод обрабатывает 3 варианта сообщений: <br>
     * <b>/start</b> - вызывает метод {@link #sendHello(Update)} <br>
     *<b>/menu</b> - вызывает метод {@link #sendMenu(Update)} (Update)} <br>
     *остальные(не пустые сообщения) - вызывает метод {@link #safeContact(Update)} (Update)}<br>
     * @param updates
     * @return
     */
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            String start = "/start";
            String menu = "/menu";
            if (update.message().text().equals(start)) {
                sendHello(update);
            } else if (update.message().text().equals(menu)) {
                sendMenu(update);
            } else if(!update.message().text().isEmpty()){
                sendAnswer(update);
            }
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * метод возвращает пользователю приветствие и ссылку на основное меню вида <i>/menu</i> <br>
     * @param update
     */
    public void sendHello(Update update) {
        String hello = "Привет, Я бот приюта для собак, для получения меню, нажмите команду " + "\n" + "/menu";
        long chatId = update.message().chat().id();
        telegramBot.execute(new SendMessage(chatId, hello));

    }

    /**
     * метод сопоставляет пользователю список разделов меню с кратким описанием сути каждого раздела <br>
     * за каждый раздел меню отвечает соотствующая константа
     * @see Shelter
     * @see Contact
     * @param update
     */
    public void sendMenu(Update update) {

        long chatId = update.message().chat().id();
        String text = "Пожалуйста выберите нужную Вам команду " +
                "\n" + COMMAND_INFORMATION_ABOUT_SHELTER.getText() + " " +
                "\n" + COMMAND_WORK_SCHEDULE_SHELTER.getText() + " " +
                "\n" + COMMAND_ADDRESS_SHELTER.getText() + " " +
                "\n" + COMMAND_DRIVING_DIRECTIONS.getText() + " " +
                "\n" + COMMAND_SAFETY_SHELTER.getText() + " " +
                "\n" + COMMAND_SAFE_CONTACT_DETAILS_FOR_COMMUNICATION.getText() + " "+
                "\n" + COMMAND_CALL_VOLUNTEER.getText();
        telegramBot.execute(new SendMessage(chatId, text));
    }

    /**
     * метод предоставляет пользователю <u>разделы меню:</u> <br>
     * с информацией о приюте - через {@link ShelterRepository#findAll()} делается запрос в БД <b>Shelter</b>  <br>
     * раздел вызова волонтера  - отправляет сообщние пользователю, что будет вызван волонтер <br>
     * раздел для предоставления пользователем его контактных данных для связи  - вызов метода {@link #safeContact(Update)}<br>
     * за каждое поле меню отвечает константа, описывающая суть информации, которая будет предоставлена
     * пользователю
     * @throws  NullPointerException если запрашиваемой инормации нет в базе данных
     * @see Shelter
     * @see Contact
     * @param update
     */
    public void sendAnswer(Update update){
        long chatId = update.message().chat().id();
        String choose = update.message().text();
        List<Shelter> shelters = shelterRepository.findAll();
        switch (choose) {
            case"/information":
                for (Shelter shelter : shelters) {
                    try {
                        String information = shelter.getInformationAboutShelter();
                        logger.warn("IMPORTANT" + information);
                        telegramBot.execute(new SendMessage(chatId, information));
                    }
                    catch(NullPointerException e){
                        logger.warn("Инфopмации в базе данных нет");
                    }
                }
                break;
            case"/workSchedule":
                for (Shelter shelter : shelters) {
                    try {
                        String workSchedule = shelter.getWorkScheduleShelter();
                        logger.warn("IMPORTANT" + workSchedule);
                        telegramBot.execute(new SendMessage(chatId, workSchedule));
                    }
                    catch(NullPointerException e){
                        logger.warn("Инфopмации в базе данных нет");
                    }
                }
                break;
            case "/address":
                for (Shelter shelter : shelters) {
                    String address = shelter.getAddressShelter();
                    logger.warn("IMPORTANT" + address);
                    telegramBot.execute(new SendMessage(chatId, address));
                }
                break;
            case "/way":
                for (Shelter shelter : shelters) {
                    try {
                        String way = shelter.getDrivingDirectionsShelter();
                        telegramBot.execute(new SendMessage(chatId, way));
                    }
                    catch(NullPointerException e){
                        logger.warn("Инфopмации в базе данных нет");

                    }
                }
                break;
            case "/safety":
                for (Shelter shelter : shelters) {
                    try {
                        String safety = shelter.getSafetyAtShelter();
                        telegramBot.execute(new SendMessage(chatId,safety));
                    }
                    catch(NullPointerException e){
                        logger.warn("Инфopмации в базе данных нет");

                    }
                }
                break;
            case "/volunteer":
                telegramBot.execute(new SendMessage(chatId, "Спасибо за обращение, волонтер приюта свяжется с Вами"));
                break;
            default:safeContact(update);


        }
    }

    /**
     * метод проверяет сообщение от пользователя на соответствие шаблону и преобразует его для внесения
     * в базу данных <br>
     * {@link ContactRepository#save(Object)} внесение инфорации о пользователе в БД <br>
     * если сообщение было введено пользователем не верно - бот вернет ответ - что данные не сохранились, так
     * как сообщение не соответсвует шаблону
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
                    + contact + ". Попробуйте еще раз.";
            telegramBot.execute(new SendMessage(chatId, warningMessage));
            logger.warn("Не смогу сохранить запись. Введенное сообщение не соотствует шаблону");
        }
    }

    public Contact findContactById(int id){
        logger.warn("check the correctness of the faculty id");
        return contactRepository.findById(id).orElse(null);
    }

    public Collection<Contact> getAllContacts(){
        return contactRepository.findAll();
    }
    public Contact addContact(Contact contact){
        return contactRepository.save(contact);
    }

}

