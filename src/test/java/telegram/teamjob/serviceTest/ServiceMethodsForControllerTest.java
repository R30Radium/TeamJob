package telegram.teamjob.serviceTest;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import telegram.teamjob.configuration.TelegramBotConfiguration;
import telegram.teamjob.entity.Contact;
import telegram.teamjob.repositories.ContactRepository;
import telegram.teamjob.repositories.ShelterRepository;
import telegram.teamjob.service.TelegramBotUpdatesListener;

import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class ServiceMethodsForControllerTest {

    @Mock
    private ContactRepository contactRepository;
    @Mock
    private ShelterRepository shelterRepository;

    @Mock
    private TelegramBotConfiguration configuration;

    @Mock
    private SendMessage message;
    @Mock
    private EditMessageText answer;


    @Mock
    TelegramBot telegramBot;
    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    public ServiceMethodsForControllerTest(){};
    @Test
    public void addContact() {
        Mockito.when(contactRepository.save(new Contact(1, "89061877772", "Иванов Иван Иванович"))).thenReturn(new Contact(1, "89061877772", "Иванов Иван Иванович"));
        Assertions.assertEquals(new Contact(1, "89061877772", "Иванов Иван Иванович"), telegramBotUpdatesListener.addContact(new Contact(1, "89061877772", "Иванов Иван Иванович")));
    }

    @Test
    public void findContactByIdPositive() {
        Contact contact = new Contact(1, "89061877772", "Иванов Иван Иванович");
        Mockito.when(contactRepository.save(contact)).thenReturn(contact);
        Assertions.assertEquals(contact, telegramBotUpdatesListener.addContact(contact));

        Mockito.when(contactRepository.findById(1)).thenReturn(Optional.of(contact));
        Assertions.assertEquals(Optional.of(contact), telegramBotUpdatesListener.findContactById(1));
    }

    @Test
    public void findContactByIdNegative() {
        Mockito.when(contactRepository.findById(10)).thenReturn(null);
        Assertions.assertNull(telegramBotUpdatesListener.findContactById(10));
    }

    @Test
    public void findAllContacts() {
        Mockito.when(contactRepository.save(new Contact(1, "89061877772", "Иванов Иван Иванович"))).thenReturn(new Contact(1, "89061877772", "Иванов Иван Иванович"));
        Assertions.assertEquals(new Contact(1, "89061877772", "Иванов Иван Иванович"), telegramBotUpdatesListener.addContact(new Contact(1, "89061877772", "Иванов Иван Иванович")));
        Mockito.when(contactRepository.save(new Contact(2, "89061977773", "Петров Петр Петрович"))).thenReturn(new Contact(2, "89061977773", "Петров Петр Петрович"));
        Assertions.assertEquals(new Contact(2, "89061977773", "Петров Петр Петрович"), telegramBotUpdatesListener.addContact(new Contact(2, "89061977773", "Петров Петр Петрович")));
        List<Contact> allContacts = List.of(
                new Contact(1, "89061877772", "Иванов Иван Иванович"),
                new Contact(2, "89061977773", "Петров Петр Петрович"));
        Mockito.when(contactRepository.findAll()).thenReturn(allContacts);
        Assertions.assertEquals(allContacts, telegramBotUpdatesListener.getAllContacts());
    }


}



