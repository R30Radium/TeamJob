package telegram.teamjob;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import telegram.teamjob.service.UserService;
import telegram.teamjob.model.User;
import telegram.teamjob.repository.UserRepository;

public class UserServiceTest {
    private UserRepository userRepositoryTest = Mockito.mock(UserRepository.class);
    private Update updateTest = Mockito.mock(Update.class);
    private Message messageTest = Mockito.mock(Message.class);
    private Chat chatTest = Mockito.mock(Chat.class);
    private UserService userServiceTest = new UserService(userRepositoryTest);

    @Test
    public void shouldCreateUser() {
        // Given часть (инициализируем что есть)
        Mockito.when(updateTest.message()).thenReturn(messageTest);
        Mockito.when(messageTest.chat()).thenReturn(chatTest);
        Mockito.when(messageTest.text()).thenReturn("User +79999999 pet");
        Mockito.when(chatTest.id()).thenReturn(534985L);
        Mockito.when(userRepositoryTest.save(Mockito.any())).thenReturn(new User());
        User userTest = new User();

        // WHen часть (Действия)
        userServiceTest.createUser(updateTest);
        userServiceTest.getUser(chatTest.id());
        // Then что то произошло и проверяем

        Mockito.verify(userRepositoryTest).save(userServiceTest.createUser(updateTest));

    }
}
