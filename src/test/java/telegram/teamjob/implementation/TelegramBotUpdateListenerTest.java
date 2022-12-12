package telegram.teamjob.implementation;


import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ResourceUtils;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;
import telegram.teamjob.repositories.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static telegram.teamjob.constants.BotMessageEnum.*;

@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdateListenerTest {

    @Test
    public void checkButtonAnswerTest1() throws IOException {
        TelegramBot tgBot = Mockito.mock(TelegramBot.class);
        TelegramBotUpdatesListener telegramBotUpdatesListener = new TelegramBotUpdatesListener(
                tgBot,
                Mockito.mock(ShelterRepository.class),
                Mockito.mock(ContactRepository.class),
                Mockito.mock(InformationForOwnerRepository.class),
                Mockito.mock(UserServiceImpl.class),
                Mockito.mock(RecordServiceImpl.class),
                Mockito.mock(PetPhotoServiceImpl.class),
                Mockito.mock(ReportRepository.class),
                Mockito.mock(PetPhotoRepository.class),
                Mockito.mock(RecordRepository.class),
                Mockito.mock(UserRepository.class),
                Mockito.mock(VolunteerRepository.class),
                Mockito.mock(VolunteerServiceImpl.class),
                Mockito.mock(ContactServiceImpl.class),
                Mockito.mock(ReportServiceImpl.class),
                Mockito.mock(ShelterServiceImpl.class)

        );
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
        TelegramBot tgBot = Mockito.mock(TelegramBot.class);
        TelegramBotUpdatesListener telegramBotUpdatesListener = new TelegramBotUpdatesListener(
                tgBot,
                Mockito.mock(ShelterRepository.class),
                Mockito.mock(ContactRepository.class),
                Mockito.mock(InformationForOwnerRepository.class),
                Mockito.mock(UserServiceImpl.class),
                Mockito.mock(RecordServiceImpl.class),
                Mockito.mock(PetPhotoServiceImpl.class),
                Mockito.mock(ReportRepository.class),
                Mockito.mock(PetPhotoRepository.class),
                Mockito.mock(RecordRepository.class),
                Mockito.mock(UserRepository.class),
                Mockito.mock(VolunteerRepository.class),
                Mockito.mock(VolunteerServiceImpl.class),
                Mockito.mock(ContactServiceImpl.class),
                Mockito.mock(ReportServiceImpl.class),
                Mockito.mock(ShelterServiceImpl.class)

        );
        telegramBotUpdatesListener = Mockito.spy(telegramBotUpdatesListener);

        String info = replacedJson("Как взять собаку из приюта");
        Update update = BotUtils.parseUpdate(info);
        telegramBotUpdatesListener.checkButtonAnswer(update);
        verify(tgBot).execute(new EditMessageText(update.callbackQuery().message().chat().id(),
                (int) update.callbackQuery().message().
                        messageId(), ANSWER_FOR_MENU_INFORMATION.getMessage()).replyMarkup(any()));
    }

    @Test
    public void checkButtonAnswerTest3() throws IOException {
        TelegramBot tgBot = Mockito.mock(TelegramBot.class);
        TelegramBotUpdatesListener telegramBotUpdatesListener = new TelegramBotUpdatesListener(
                tgBot,
                Mockito.mock(ShelterRepository.class),
                Mockito.mock(ContactRepository.class),
                Mockito.mock(InformationForOwnerRepository.class),
                Mockito.mock(UserServiceImpl.class),
                Mockito.mock(RecordServiceImpl.class),
                Mockito.mock(PetPhotoServiceImpl.class),
                Mockito.mock(ReportRepository.class),
                Mockito.mock(PetPhotoRepository.class),
                Mockito.mock(RecordRepository.class),
                Mockito.mock(UserRepository.class),
                Mockito.mock(VolunteerRepository.class),
                Mockito.mock(VolunteerServiceImpl.class),
                Mockito.mock(ContactServiceImpl.class),
                Mockito.mock(ReportServiceImpl.class),
                Mockito.mock(ShelterServiceImpl.class)


        );

        String info2 = replacedJson("Прислать отчет о питомце");
        Update update2 = BotUtils.parseUpdate(info2);
        telegramBotUpdatesListener.checkButtonAnswer(update2);
        verify(tgBot).execute(new EditMessageText(update2.callbackQuery().message().chat().id(),
                (int) update2.callbackQuery().message().
                        messageId(), ANSWER_FOR_MENU_REPORT.getMessage()).replyMarkup(any()));
    }

    @Test
    public void checkButtonAnswerTest4() throws IOException {
        TelegramBot tgBot = Mockito.mock(TelegramBot.class);
        TelegramBotUpdatesListener telegramBotUpdatesListener = new TelegramBotUpdatesListener(
                tgBot,
                Mockito.mock(ShelterRepository.class),
                Mockito.mock(ContactRepository.class),
                Mockito.mock(InformationForOwnerRepository.class),
                Mockito.mock(UserServiceImpl.class),
                Mockito.mock(RecordServiceImpl.class),
                Mockito.mock(PetPhotoServiceImpl.class),
                Mockito.mock(ReportRepository.class),
                Mockito.mock(PetPhotoRepository.class),
                Mockito.mock(RecordRepository.class),
                Mockito.mock(UserRepository.class),
                Mockito.mock(VolunteerRepository.class),
                Mockito.mock(VolunteerServiceImpl.class),
                Mockito.mock(ContactServiceImpl.class),
                Mockito.mock(ReportServiceImpl.class),
                Mockito.mock(ShelterServiceImpl.class)



        );
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
        TelegramBot tgBot = Mockito.mock(TelegramBot.class);
        TelegramBotUpdatesListener telegramBotUpdatesListener = new TelegramBotUpdatesListener(
                tgBot,
                Mockito.mock(ShelterRepository.class),
                Mockito.mock(ContactRepository.class),
                Mockito.mock(InformationForOwnerRepository.class),
                Mockito.mock(UserServiceImpl.class),
                Mockito.mock(RecordServiceImpl.class),
                Mockito.mock(PetPhotoServiceImpl.class),
                Mockito.mock(ReportRepository.class),
                Mockito.mock(PetPhotoRepository.class),
                Mockito.mock(RecordRepository.class),
                Mockito.mock(UserRepository.class),
                Mockito.mock(VolunteerRepository.class),
                Mockito.mock(VolunteerServiceImpl.class),
                Mockito.mock(ContactServiceImpl.class),
                Mockito.mock(ReportServiceImpl.class),
                Mockito.mock(ShelterServiceImpl.class)


        );
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

    @Test
    public void sendResponseForThirdMenu1Test() throws IOException {
        TelegramBot tgBot = Mockito.mock(TelegramBot.class);
        TelegramBotUpdatesListener telegramBotUpdatesListener = new TelegramBotUpdatesListener(
                tgBot,
                Mockito.mock(ShelterRepository.class),
                Mockito.mock(ContactRepository.class),
                Mockito.mock(InformationForOwnerRepository.class),
                Mockito.mock(UserServiceImpl.class),
                Mockito.mock(RecordServiceImpl.class),
                Mockito.mock(PetPhotoServiceImpl.class),
                Mockito.mock(ReportRepository.class),
                Mockito.mock(PetPhotoRepository.class),
                Mockito.mock(RecordRepository.class),
                Mockito.mock(UserRepository.class),
                Mockito.mock(VolunteerRepository.class),
                Mockito.mock(VolunteerServiceImpl.class),
                Mockito.mock(ContactServiceImpl.class),
                Mockito.mock(ReportServiceImpl.class),
                Mockito.mock(ShelterServiceImpl.class)

        );
        telegramBotUpdatesListener = Mockito.spy(telegramBotUpdatesListener);

        String info = replacedJson("record");
        Update update = BotUtils.parseUpdate(info);
        telegramBotUpdatesListener.sendResponseForThirdMenu(update);
        verify(tgBot).execute(new EditMessageText(update.callbackQuery().message().chat().id(),
                update.callbackQuery().message().messageId(),RECORD.getMessage()));

    }
    @Test
    public void sendResponseForThirdMenu2Test() throws IOException {
        TelegramBot tgBot = Mockito.mock(TelegramBot.class);
        TelegramBotUpdatesListener telegramBotUpdatesListener = new TelegramBotUpdatesListener(
                tgBot,
                Mockito.mock(ShelterRepository.class),
                Mockito.mock(ContactRepository.class),
                Mockito.mock(InformationForOwnerRepository.class),
                Mockito.mock(UserServiceImpl.class),
                Mockito.mock(RecordServiceImpl.class),
                Mockito.mock(PetPhotoServiceImpl.class),
                Mockito.mock(ReportRepository.class),
                Mockito.mock(PetPhotoRepository.class),
                Mockito.mock(RecordRepository.class),
                Mockito.mock(UserRepository.class),
                Mockito.mock(VolunteerRepository.class),
                Mockito.mock(VolunteerServiceImpl.class),
                Mockito.mock(ContactServiceImpl.class),
                Mockito.mock(ReportServiceImpl.class),
                Mockito.mock(ShelterServiceImpl.class)


        );
        telegramBotUpdatesListener = Mockito.spy(telegramBotUpdatesListener);

        String info = replacedJson("photo");
        Update update = BotUtils.parseUpdate(info);
        long chatId = update.callbackQuery().message().chat().id();
        int messageId = update.callbackQuery().message().messageId();
        telegramBotUpdatesListener.sendResponseForThirdMenu(update);
        verify(tgBot).execute(new EditMessageText(chatId, messageId, PHOTO.getMessage()));
    }


    @Test
    public void  checkAvailabilityOfReportAndSendRemindeTest(){
        TelegramBot tgBot = Mockito.mock(TelegramBot.class);
        TelegramBotUpdatesListener telegramBotUpdatesListener = new TelegramBotUpdatesListener(
                tgBot,
                Mockito.mock(ShelterRepository.class),
                Mockito.mock(ContactRepository.class),
                Mockito.mock(InformationForOwnerRepository.class),
                Mockito.mock(UserServiceImpl.class),
                Mockito.mock(RecordServiceImpl.class),
                Mockito.mock(PetPhotoServiceImpl.class),
                Mockito.mock(ReportRepository.class),
                Mockito.mock(PetPhotoRepository.class),
                Mockito.mock(RecordRepository.class),
                Mockito.mock(UserRepository.class),
                Mockito.mock(VolunteerRepository.class),
                Mockito.mock(VolunteerServiceImpl.class),
                Mockito.mock(ContactServiceImpl.class),
                Mockito.mock(ReportServiceImpl.class),
                Mockito.mock(ShelterServiceImpl.class)


        );
        telegramBotUpdatesListener = Mockito.spy(telegramBotUpdatesListener);
        long chatId = 23977364L;
        LocalDateTime dateTime = LocalDateTime.parse("1986-04-08T12:30:00");
        telegramBotUpdatesListener.checkAvailabilityOfReportAndSendReminder(chatId, dateTime);
        verify(tgBot).execute(new SendMessage(chatId, REPORT_REMEMBER.getMessage() + dateTime));
    }


}
