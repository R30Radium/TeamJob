package telegram.teamjob.implementation;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.service.RecordService;
import telegram.teamjob.entity.User;
import telegram.teamjob.repositories.RecordRepository;
import telegram.teamjob.repositories.UserRepository;
import telegram.teamjob.entity.Record;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import static telegram.teamjob.constants.BotMessageEnum.*;
import static telegram.teamjob.constants.BotMessageEnum.SAVE_INFORMATION;
@Service
public class RecordServiceImpl implements RecordService {
    private Logger logger = LoggerFactory.getLogger(RecordServiceImpl.class);
    private final RecordRepository recordRepository;
    private TelegramBot telegramBot;


    public RecordServiceImpl(RecordRepository recordRepository,
                             TelegramBot telegramBot) {
        this.recordRepository = recordRepository;
        this.telegramBot = telegramBot;

    }

    @Override
    public Long findRecordId(Update update) {
        Long chatId = update.message().chat().id();
        LinkedList<Record> recordsList = recordRepository.findAllRecordByChatId(chatId);
        Record record = recordsList.peekLast();
        if (record != null) {
            return record.getRecordId();
        } else {
            //Добавить условие (Если есть в БД)
            return recordRepository.findRecordByChatId(chatId).getRecordId();
        }
    }
    /**
     * метод сохраняет в базе данных часть отчета отвечающую за информацию  <br>
     * об измеении в поведении питомца <br>
     * see ChangeInBehavior
     * @param update
     */

    public void saveRecord(Update update) {
        LocalDateTime localDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        String record = update.message().text();
        StringBuilder builder = new StringBuilder(record);
        builder.delete(0,6);
        String result = builder.toString();
        long chatId = update.message().chat().id();
        int one = result.indexOf("рацион");
        int two = result.indexOf("самочувствие");
        String diet = result.substring(one, two);
        StringBuilder builder2 = new StringBuilder(diet);
        builder2.delete(0, 7);
        String dietResult = builder2.toString();

        int three = result.indexOf("самочувствие");
        int four = result.indexOf("поведение");
        String adaptation = result.substring(three,four);
        StringBuilder builder3 = new StringBuilder(adaptation);
        builder3.delete(0, 13);
        String adaptationResult = builder3.toString();

        String behavior = result.substring(four);
        StringBuilder builder4 = new StringBuilder(behavior);
        builder4.delete(0, 10);
        String behaviorResult = builder4.toString();

        Record recordForBase = new Record();
        recordForBase.setDiet(dietResult);
        recordForBase.setAdaptation(adaptationResult);
        recordForBase.setChangeInBehavior(behaviorResult);
        recordForBase.setChatId(chatId);
        recordForBase.setDateTime(localDate);


        if (dietResult.length() < 8){
            telegramBot.execute(new SendMessage(chatId,RECORD_DIETA.getMessage()));
        }
        else if(adaptationResult.length() < 8){
            telegramBot.execute(new SendMessage(chatId, RECORD_ADAPTATION.getMessage()));
        }
        else if(behaviorResult.length()< 8) {
            telegramBot.execute(new SendMessage(chatId,RECORD_BEHAVIOR.getMessage()));
        }
        recordRepository.save(recordForBase);
        telegramBot.execute(new SendMessage(chatId, SAVE_INFORMATION.getMessage()));
        logger.info("текстовый отчет занесен в базу данных");

    }


}
