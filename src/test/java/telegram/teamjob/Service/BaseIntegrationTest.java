package telegram.teamjob.Service;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import telegram.teamjob.constants.BotMessageEnum;
import telegram.teamjob.entity.PetPhoto;
import telegram.teamjob.entity.Record;
import telegram.teamjob.entity.Shelter;
import telegram.teamjob.entity.User;
import telegram.teamjob.repositories.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static telegram.teamjob.Service.TelegramBotUpdatesListener.PATTERN;
import static telegram.teamjob.constants.BotMessageEnum.*;
import static telegram.teamjob.constants.BotButtonEnum.*;
import static telegram.teamjob.implementation.TestUtils.*;


@SpringBootTest
@ActiveProfiles("test")
public class BaseIntegrationTest {
    @MockBean
    private TelegramBot telegramBot;

    private final Update helloUpdate = BotUtils.parseUpdate(getFileContent("classpath:updates/update_1.json"));
    private final Update userCreationAnswer = BotUtils.parseUpdate(getFileContent("classpath:updates/update_2.json"));
    private final Update createRecordAnswer = BotUtils.parseUpdate(getFileContent("classpath:updates/update_3.json"));
    private final Update userCreationUpdate = BotUtils.parseUpdate(getFileContent("classpath:updates/update_create_user.json"));
    private final Update photoUpload = BotUtils.parseUpdate(getFileContent("classpath:updates/update_photoUpload.json"));
    @Autowired
    private TelegramBotUpdatesListener telegramBotUpdatesListener;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private PetPhotoRepository petPhotoRepository;

    @BeforeEach
    public void cleanup() {
        petPhotoRepository.deleteAll();
        recordRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testHelloMessage() {
        telegramBotUpdatesListener.process(List.of(helloUpdate));

        ArgumentCaptor<SendMessage> sentMessage = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(sentMessage.capture());

        assertEquals(helloUpdate.message().chat().id(), sentMessage.getValue().getParameters().get("chat_id"));
        assertEquals(("@" + helloUpdate.message().chat().username() + BotMessageEnum.START_MESSAGE.getMessage())
                , sentMessage.getValue().getParameters().get("text"));
    }

    @Test
    public void testUserCreatMessage() {
        telegramBotUpdatesListener.process(List.of(userCreationAnswer));
        ArgumentCaptor<SendMessage> sentMessage = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(sentMessage.capture());

        assertEquals(userCreationAnswer.message().chat().id(), sentMessage.getValue().getParameters().get("chat_id"));
        assertEquals(BotMessageEnum.CREATE_USER_INFO.getMessage()
                , sentMessage.getValue().getParameters().get("text"));

    }

    @Test
    public void testSendRecordMessage() {
        telegramBotUpdatesListener.process(List.of(createRecordAnswer));
        ArgumentCaptor<SendMessage> sentMessage = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(sentMessage.capture());

        assertEquals(createRecordAnswer.message().chat().id(), sentMessage.getValue().getParameters().get("chat_id"));
        assertEquals(BotMessageEnum.DAILY_RECORD_INFO.getMessage()
                , sentMessage.getValue().getParameters().get("text"));
    }

    @Test
    public void testUserCreation() {
        String text = userCreationUpdate.message().text();
        Matcher matcher = PATTERN.matcher(text);
        if (matcher.matches()) {
            String phone = matcher.group(1);
            String username = matcher.group(3);
            // String pet = matcher.group(3);
            telegramBotUpdatesListener.process(List.of(userCreationUpdate));

            User createdUser = userRepository.findByChatId(userCreationUpdate.message().chat().id());
            assertEquals(username, createdUser.getUserName());
            assertEquals(phone, createdUser.getNumberPhone());
            // assertEquals(pet, createdUser.getPetName());
        }
    }

    @Test
    public void testPhotoUpload() {
        LocalDateTime localDateTime = LocalDateTime.now();
        cleanup();//Не работает анотация BeforeEach
        testUserCreation();
        User user = userRepository.findByChatId(-23123123123123L);
        Record record = new Record();
        record.setUser(user);
        record.setDateTime(localDateTime);
        record.setChatId(-23123123123123L);
        record.setLifeRecord("Test");

        recordRepository.save(record);
        telegramBotUpdatesListener.process(List.of(photoUpload));

        PhotoSize[] photoSizes = photoUpload.message().photo();
        Iterable<PetPhoto> photoList = petPhotoRepository.findAll();
        PetPhoto petPhoto = photoList.iterator().next();
        String[] path = petPhoto.getFilePath().split("\\.");
        String testUniqueId = path[1];
        System.out.println(testUniqueId);
        assertEquals(photoSizes[0].fileUniqueId(), testUniqueId);
    }

    @ParameterizedTest
    @CsvSource({"'info', 'Информация о приюте'", "'workTime', '24/7 без перерывов'", "'address', 'Улица чертовых тестов'", "'way', 'Путь через боль'",
            "'safety','тестовый ответ'", "'volunteer', 'Спасибо за обращение, волонтер приюта свяжется с Вами'",
            "'contact', 'Если Вы хотите оставить свои КОНТАКТНЫЕ ДАННЫЕ для связи, введите их строго в соотвествии с шаблоном: \n89061877772 Иванов Иван Иванович'",
            "'rules', 'Забери 2 собаки'", "'docs','Документы'", "'transportation', 'Перевозка'", "'arrangementPuppy','Щеночки'",
            "'arrangementDog','Собачки'", "'arrangementDogInvalid','Собачки 2'", "'cynologist','Тест'", "'goodCynologists','Тест 2'", "'reject','Тест3'"})
    public void testResponseFirstAndSecondMenu(String callbacks, String answer) throws IOException, URISyntaxException {
        TelegramBot tgBot = Mockito.mock(TelegramBot.class);
        TelegramBotUpdatesListener telegramBotUpdatesListener = new TelegramBotUpdatesListener(
                tgBot,
                Mockito.mock(ShelterRepository.class),
                Mockito.mock(ContactRepository.class),
                Mockito.mock(UserRepository.class),
                Mockito.mock(InformationForOwnerRepository.class),
                Mockito.mock(RecordServiceImpl.class),
                Mockito.mock(PetPhotoServiceImpl.class)
        );
        telegramBotUpdatesListener = Mockito.spy(telegramBotUpdatesListener);

        String info = replacedJson(callbacks);
        Update update = BotUtils.parseUpdate(info);
        System.out.println(update.toString());
        ShelterRepository shelterRepository = Mockito.mock(ShelterRepository.class);
        Shelter shelter = Mockito.mock(Shelter.class);
        List<Shelter> shelterList = new ArrayList<>();
        shelterList.add(shelter);
        when(shelterRepository.findAll()).thenReturn(shelterList);
        telegramBotUpdatesListener.messageHandler(update);
        ArgumentCaptor<EditMessageText> sentMessage = ArgumentCaptor.forClass(EditMessageText.class);
        verify(tgBot).execute(sentMessage.capture());
        assertEquals(answer, sentMessage.getValue().getParameters().get("text"));
    }

    private String replacedJson(String replacement) throws IOException, URISyntaxException {
        String json = Files.readString(Paths.get(BaseIntegrationTest.class.getResource("update_Buttoms.json").toURI()));
        return json.replace("%text%", replacement);
    }

}
