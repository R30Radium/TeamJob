package telegram.teamjob.implementation;


import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ResourceUtils;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;
import telegram.teamjob.repositories.UserRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.verify;
import static telegram.teamjob.constants.BotMessageEnum.SAVE_INFORMATION;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Test
    public void  saveRecordTest() throws IOException {
        TelegramBot tgBot = Mockito.mock(TelegramBot.class);
        UserServiceImpl userService = new UserServiceImpl(
                tgBot,
                Mockito.mock(UserRepository.class)
        );
        userService = Mockito.spy(userService);

        String info = replacedJson2("Шарик(пес) 89061877772 Иванов Иван Иванович ");
        Update update = BotUtils.parseUpdate(info);
        userService.createUser(update);
        verify(tgBot).execute(new SendMessage(update.message().chat().id(),   SAVE_INFORMATION.getMessage()));
    }

    public String replacedJson2(String replacement) throws IOException {
        String json = FileUtils.readFileToString(ResourceUtils.getFile("classpath:updates/update_4.json"),
                StandardCharsets.UTF_8);
        return json.replace("%data%", replacement);
    }
}

