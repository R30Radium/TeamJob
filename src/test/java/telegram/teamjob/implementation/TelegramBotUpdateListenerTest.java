package telegram.teamjob.implementation;


import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ResourceUtils;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;
import telegram.teamjob.repositories.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static telegram.teamjob.constants.BotMessageEnum.*;

@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdateListenerTest {
    private TelegramBot tgBot = Mockito.mock(TelegramBot.class);
    private TelegramBotUpdatesListener telegramBotUpdatesListener = new TelegramBotUpdatesListener(
            Mockito.mock(UserRepository.class),
            Mockito.mock(RecordRepository.class),
            Mockito.mock(PetPhotoRepository.class),
            tgBot,
            Mockito.mock(ShelterRepository.class),
            Mockito.mock(ContactRepository.class),
            Mockito.mock(InformationForOwnerRepository.class),
            Mockito.mock(UserServiceImpl.class),
            Mockito.mock(RecordServiceImpl.class),
            Mockito.mock(PetPhotoServiceImpl.class),
            Mockito.mock(VolunteerRepository.class),
            Mockito.mock(VolunteerServiceImpl.class),
            Mockito.mock(ContactServiceImpl.class),
            Mockito.mock(ReportServiceImpl.class),
            Mockito.mock(ShelterServiceImpl.class),
            clientService);

    @Test
    public void checkButtonAnswerTest1() throws IOException {
        telegramBotUpdatesListener = Mockito.spy(telegramBotUpdatesListener);
        String info = replacedJson("Узнать информацию о приюте");
        Update update = BotUtils.parseUpdate(info);
        telegramBotUpdatesListener.checkButtonAnswer(update);
        verify(tgBot).execute(new SendMessage(update.callbackQuery().message().chat().id(), "Вы выбрали раздел " + "\n" + "\"Узнать информацию о приюте\". " + "\n"
                + "Ознакомьтесь пожалуйста " +
                "с меню и выберите интересующий вас пункт").replyMarkup(any()));
    }

    private String replacedJson(String replacement) throws IOException {
        String json = FileUtils.readFileToString(ResourceUtils.getFile("classpath:updates/update_test.json"),
                StandardCharsets.UTF_8);
        return json.replace("%data%", replacement);
    }

    @Test
    public void checkButtonAnswerTest2() throws IOException, URISyntaxException {
        String info = replacedJson("Как взять собаку из приюта");
        Update update = BotUtils.parseUpdate(info);
        telegramBotUpdatesListener.checkButtonAnswer(update);
        verify(tgBot).execute(new EditMessageText(update.callbackQuery().message().chat().id(),
                (int) update.callbackQuery().message().
                        messageId(), ANSWER_FOR_MENU_INFORMATION.getMessage()).replyMarkup(any()));
    }

    @Test
    public void checkButtonAnswerTest3() throws IOException {
        String info2 = replacedJson("Прислать отчет о питомце");
        Update update2 = BotUtils.parseUpdate(info2);
        telegramBotUpdatesListener.checkButtonAnswer(update2);
        verify(tgBot).execute(new EditMessageText(update2.callbackQuery().message().chat().id(),
                (int) update2.callbackQuery().message().
                        messageId(), ANSWER_FOR_MENU_REPORT.getMessage()).replyMarkup(any()));
    }

    @Test
    public void checkButtonAnswerTest4() throws IOException {
        telegramBotUpdatesListener = Mockito.spy(telegramBotUpdatesListener);

        String info = replacedJson("Позвать волонтера");
        Update update = BotUtils.parseUpdate(info);
        telegramBotUpdatesListener.checkButtonAnswer(update);
        verify(tgBot).execute(new EditMessageText(update.callbackQuery().message().chat().id(),
                (int) update.callbackQuery().message().
                        messageId(), ASK_HELP.getMessage()).replyMarkup(any()));
    }

    @Test
    public void sendGreetingMessageTest() throws IOException {
        telegramBotUpdatesListener = Mockito.spy(telegramBotUpdatesListener);

        String info = replacedJson2("/start");
        Update update = BotUtils.parseUpdate(info);
        String greetingMessage = update.message().chat().username() + START_MESSAGE.getMessage();
        telegramBotUpdatesListener.sendGreetingMessage(update);
        verify(tgBot).execute(new SendMessage(update.message().chat().id(), greetingMessage)
                .replyMarkup(any()));
    }

    private String replacedJson2(String replacement) throws IOException {
        String json = FileUtils.readFileToString(ResourceUtils.getFile("classpath:updates/update_4.json"),
                StandardCharsets.UTF_8);
        return json.replace("%data%", replacement);
    }


    public void sendResponseForThirdMenu1Test() throws IOException {
        telegramBotUpdatesListener = Mockito.spy(telegramBotUpdatesListener);

        String info = replacedJson("record");
        Update update = BotUtils.parseUpdate(info);
        long chatId = update.callbackQuery().message().chat().id();
        //telegramBotUpdatesListener.sendResponseForThirdMenu(update);

        ArgumentCaptor<SendMessage> actual = ArgumentCaptor.forClass(SendMessage.class); // объект для захвата аргумента
        verify(tgBot).execute(actual.capture()); // проверяем, что вызвался метод и говорим, чтобы аргумен захватился

        Map<String, Object> parameters = actual.getValue().getParameters(); // достаём из захваченного аргумента параметр и проверяем
        Assertions.assertThat(parameters.get("chat_id")).isEqualTo(chatId);
        Assertions.assertThat(parameters.get("text")).isEqualTo(RECORD.getMessage());
    }


    public void sendResponseForThirdMenu2Test() throws IOException {

        telegramBotUpdatesListener = Mockito.spy(telegramBotUpdatesListener);

        String info = replacedJson("photo");
        Update update = BotUtils.parseUpdate(info);
        long chatId = update.callbackQuery().message().chat().id();
       // telegramBotUpdatesListener.sendResponseForThirdMenu(update);
//        verify(tgBot).execute(ArgumentMatchers.<SendMessage>argThat(actual -> {
//            Map<String, Object> parameters = actual.getParameters();
//            return Objects.equals(parameters.get("chat_id"), chatId)
//                    && Objects.equals(parameters.get("data"), PHOTO.getMessage());
//        }));
        ArgumentCaptor<SendMessage> actual = ArgumentCaptor.forClass(SendMessage.class); // объект для захвата аргумента
        verify(tgBot).execute(actual.capture()); // проверяем, что вызвался метод и говорим, чтобы аргумен захватился

        Map<String, Object> parameters = actual.getValue().getParameters(); // достаём из захваченного аргумента параметр и проверяем
        Assertions.assertThat(parameters.get("chat_id")).isEqualTo(chatId);
        Assertions.assertThat(parameters.get("text")).isEqualTo(PHOTO.getMessage());

    }
}
