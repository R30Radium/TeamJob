package telegram.teamjob.implementation;


import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ResourceUtils;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;
import telegram.teamjob.repositories.ContactRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import static org.mockito.Mockito.verify;
import static telegram.teamjob.constants.BotMessageEnum.SAVE_INFORMATION_CONTACT;

@ExtendWith(MockitoExtension.class)
public class ContactServiceImplTest {

    @Test
    public void  saveContactTest() throws IOException {
        TelegramBot tgBot = Mockito.mock(TelegramBot.class);
        ContactServiceImpl contactService = new ContactServiceImpl(
                tgBot,
                Mockito.mock(ContactRepository.class)
        );

        contactService = Mockito.spy(contactService);

        String info = replacedJson2("89061877772 Иванов Иван Иванович ");
        Update update = BotUtils.parseUpdate(info);
        contactService.saveContact(update);
     verify(tgBot).execute(ArgumentMatchers.<SendMessage>argThat(actual->{
    Map<String,Object> parameters = actual.getParameters();
    return Objects.equals(parameters.get("chat_id"),update.message().chat().id())
            && Objects.equals(parameters.get("text"), SAVE_INFORMATION_CONTACT.getMessage());
}));
    }

    public String replacedJson2(String replacement) throws IOException {
        String json = FileUtils.readFileToString(ResourceUtils.getFile("classpath:updates/update_4.json"),
                StandardCharsets.UTF_8);
        return json.replace("%data%", replacement);
    }


}



