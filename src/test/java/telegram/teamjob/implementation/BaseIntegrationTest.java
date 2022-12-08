package telegram.teamjob.implementation;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import telegram.teamjob.constants.BotMessageEnum;
import telegram.teamjob.entity.User;
import telegram.teamjob.repositories.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
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
    @Autowired
    private TelegramBotUpdatesListener telegramBotUpdatesListener;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    public void testHelloMessage() {
        telegramBotUpdatesListener.process(List.of(helloUpdate));

        ArgumentCaptor<SendMessage> sentMessage = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(sentMessage.capture());

        assertEquals(helloUpdate.message().chat().id(), sentMessage.getValue().getParameters().get("chat_id"));
        assertEquals(BotMessageEnum.START_MESSAGE.getMessage()
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
        String[] splittedMessage = userCreationUpdate.message().text().split("\\s");
        String username = splittedMessage[0];
        String phone = splittedMessage[1];
        String pet = splittedMessage[2];

        telegramBotUpdatesListener.process(List.of(userCreationUpdate));

        User createdUser = userRepository.findByChatId(userCreationUpdate.message().chat().id());
        assertEquals(username, createdUser.getUserName());
        assertEquals(phone, createdUser.getNumberPhone());
        assertEquals(pet, createdUser.getPetName());
    }


}
