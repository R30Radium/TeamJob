package telegram.teamjob.repositories;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import telegram.teamjob.entity.Record;
import telegram.teamjob.implementation.*;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static telegram.teamjob.constants.BotMessageEnum.REPORT_REMEMBER;
import static telegram.teamjob.constants.BotMessageEnum.START_MESSAGE;

@ExtendWith(MockitoExtension.class)
public class RecordRepositoryTest {
    @Mock
   private  RecordRepository recordRepository;
    @InjectMocks
    private RecordServiceImpl recordService;
    @Mock
    Record record;

    private final LocalDateTime dateTime = LocalDateTime.of(2022,12,13,13,57,00);
    private final Record record1 = new Record(1L, 9809L, dateTime, "dfhkhfd", "ldjfkldjf", "kdjfjf" );
   private final List<Record> records= List.of(
           new Record(1L, 9809L, dateTime, "dfhkhfd", "ldjfkldjf", "kdjfjf" )
   );

    @Test
    public void checkAvailabilityOfReportAndSendRemindeTest(){


        Mockito.when(recordRepository.save(record)).thenReturn(record);
        Assertions.assertEquals(record, recordRepository.save(record));
        Mockito.when(recordRepository.findAllRecordByChatIdAndDateTime(9809L, dateTime)).
                thenReturn(records);
        Assertions.assertEquals(records, recordRepository.findAllRecordByChatIdAndDateTime(9809L, dateTime));

    }

    /*
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
        long chatId = 346463L;
        Mockito.when(recordRepository.save(record)).thenReturn(record);
        Assertions.assertEquals(record, recordRepository.save(record));

        telegramBotUpdatesListener.checkAvailabilityOfReportAndSendReminder(chatId,dateTime);
        Mockito.when(recordRepository.findAllRecordByChatIdAndDateTime(9809L, dateTime)).
                thenReturn(records);
        Assertions.assertEquals(records, recordRepository.findAllRecordByChatIdAndDateTime(9809L, dateTime));
       // verify(tgBot).execute(new SendMessage(chatId, REPORT_REMEMBER.getMessage() + dateTime.toString()));

    }
*/
}
