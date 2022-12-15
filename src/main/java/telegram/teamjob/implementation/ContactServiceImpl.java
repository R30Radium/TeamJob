package telegram.teamjob.implementation;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.entity.Contact;
import telegram.teamjob.entity.User;
import telegram.teamjob.repositories.ContactRepository;
import telegram.teamjob.Service.ContactService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static telegram.teamjob.constants.BotButtonForShelterMenuEnum.BOT_ANSWER_NOT_SAVED_CONTACT;
import static telegram.teamjob.constants.BotMessageEnum.*;
import static telegram.teamjob.implementation.TelegramBotUpdatesListener.CONTACT_TEXT_PATTERN;

@Service
public class ContactServiceImpl implements ContactService {
    static private final Pattern pattern2 = Pattern.compile(CONTACT_TEXT_PATTERN);
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private  final ContactRepository contactRepository;
    private TelegramBot telegramBot;

    ContactServiceImpl(TelegramBot telegramBot, ContactRepository contactRepository){
        this.telegramBot = telegramBot;
        this.contactRepository = contactRepository;

    }
    @Override
    public void saveContact(Update update){
        logger.info("Сохранение контакта");
        String text = update.message().text();
        long chatId = update.message().chat().id();
        LocalDateTime localDateTime =  LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Matcher matcher = pattern2.matcher(text);
        if (matcher.matches()) {
            String phone = matcher.group(1);
            String name = matcher.group(3);
            Contact contact = new Contact();
            contact.setNumberPhone(phone);
            contact.setName(name);
            contact.setLocalDateTime(localDateTime);
            List<Contact> contacts = contactRepository.findContactByNumberPhoneAndName(phone,name);
            logger.info(String.valueOf(contacts));

            try{
                if(contacts.isEmpty()){
                    contactRepository.save(contact);
                    telegramBot.execute(new SendMessage(chatId, SAVE_INFORMATION_CONTACT.getMessage()));
                    logger.info("Данные сохранены в базе данных");
                } else {
                    logger.info("такой контакт уже есть в базе данных");
                    telegramBot.execute(new SendMessage(chatId, HAVE_CONTACT.getMessage()));
                }
            }
            catch(NullPointerException e){
                logger.info("пользователь предоставил новый контакт");
            }
        }
        else if(!matcher.matches()){
            telegramBot.execute(new SendMessage(chatId, BOT_ANSWER_NOT_SAVED_CONTACT.getText()));
            logger.warn("Не смогу сохранить запись. Введенное сообщение не соотствует шаблону");
        }
    }


    @Override
    public void getAllContactsForVolunteer(Update update) {
        telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(),
                DELETE_ALL_CONTACTS.getMessage()));
        List<Contact> allContacts = contactRepository.findAll();
        if (allContacts.isEmpty()) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(),
                    NOT_FIND_CONTACTS_FOR_VOLUNTEER.getMessage()));
        }
        else{

            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(),
                    FIND_CONTACTS_FOR_VOLUNTEER.getMessage() + allContacts));
        }
    }
    @Override
    public void deleteAllContacts(Update update) {
        contactRepository.deleteAll();
        List<Contact> allContacts = contactRepository.findAll();
        if (allContacts.isEmpty()) {
            telegramBot.execute(new SendMessage(update.message().chat().id(),
                    DONE.getMessage()));
        }
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
