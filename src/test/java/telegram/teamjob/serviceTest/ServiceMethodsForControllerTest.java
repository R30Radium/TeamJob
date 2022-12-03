package telegram.teamjob.serviceTest;


import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;


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

    @Test
    public void verifyTests() throws Exception {
        String json = Files.readString(Paths.get("update.json"));
        Update update = BotUtils.parseUpdate(json);
        TelegramBotUpdatesListener telegramBotUpdatesListener1 = Mockito.mock(TelegramBotUpdatesListener.class);
        telegramBotUpdatesListener1.safeContact(update);
        telegramBotUpdatesListener1.sendResponseForFirstAndSecondMenu(update);
        telegramBotUpdatesListener1.checkButtonAnswer(update);
        telegramBotUpdatesListener1.sendGreetingMessage(update);
        telegramBotUpdatesListener1.checkAnswer(update);
        telegramBotUpdatesListener1.process(List.of(update));


        verify(telegramBotUpdatesListener1).safeContact(update);
        verify(telegramBotUpdatesListener1).sendResponseForFirstAndSecondMenu(update);
        verify(telegramBotUpdatesListener1).checkButtonAnswer(update);
        verify(telegramBotUpdatesListener1).sendGreetingMessage(update);
        verify(telegramBotUpdatesListener1).checkAnswer(update);
        verify(telegramBotUpdatesListener1).process(List.of(update));


        ContactRepository contactRepository1 = Mockito.mock(ContactRepository.class);
        contactRepository1.findContactByNumberPhoneAndName("89061877772", "Иванов Иван Иванович");
        verify(contactRepository1).findContactByNumberPhoneAndName("89061877772", "Иванов Иван Иванович");
        contactRepository1.findById(1);
        verify(contactRepository1).findById(1);

    }
    @Test
    void negativeSendMessage() {
        RestAssured.baseURI = "https://api.telegram.org/5758859832:AAEwJ4cIzXZnXITbJ1CnX1sy1K7WmXW-uhc";
        given()
                .param("text", "rest-assured_TEST")
                .param("chat_id", "362396673")
                .when()
                .get("/start")
                .then()
                .statusCode(404);
    }
    @Test
    public void whenRequestGet_thenOK(){
        when().request("GET", "/contact/20").then().statusCode(200);
    }




}



