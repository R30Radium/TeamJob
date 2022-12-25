package telegram.teamjob.implementation.Cat;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.Service.ContactService;
import telegram.teamjob.entity.Contact;
import telegram.teamjob.entity.Cat.ContactCat;
import telegram.teamjob.repositories.Cat.ContactCatRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static telegram.teamjob.constants.BotButtonForShelterMenuEnum.BOT_ANSWER_NOT_SAVED_CONTACT;
import static telegram.teamjob.constants.BotMessageEnum.*;
import static telegram.teamjob.implementation.TelegramBotUpdatesListener.CONTACT_TEXT_PATTERN;
@Service
public class ContactCatServiceImpl implements ContactService {
    static private final Pattern pattern2 = Pattern.compile(CONTACT_TEXT_PATTERN);
    private Logger logger = LoggerFactory.getLogger(ContactCatServiceImpl.class);
    private  final ContactCatRepository contactCatRepository;
    private TelegramBot telegramBot;

    public ContactCatServiceImpl(ContactCatRepository contactCatRepository,TelegramBot telegramBot){
        this.contactCatRepository = contactCatRepository;
        this.telegramBot = telegramBot;
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
            ContactCat contactCat = new ContactCat();
            contactCat.setNumberPhone(phone);
            contactCat.setName(name);
            contactCat.setLocalDateTime(localDateTime);
            List<Contact> contacts = contactCatRepository.findContactByNumberPhoneAndName(phone,name);
            logger.info(String.valueOf(contacts));

            try{
                if(contacts.isEmpty()){
                    contactCatRepository.save(contactCat);
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
        List<ContactCat> allContacts = contactCatRepository.findAll();
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
        contactCatRepository.deleteAll();
        List<ContactCat> allContacts = contactCatRepository.findAll();
        if (allContacts.isEmpty()) {
            telegramBot.execute(new SendMessage(update.message().chat().id(),
                    DONE.getMessage()));
        }
    }
}
