package telegram.teamjob.implementation;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.Service.RecordService;
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
public class RecordServiceImpl implements RecordService{
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
        } else if (recordRepository.findAll().isEmpty()) {
            telegramBot.execute(new SendMessage(chatId, "Для начала отчёт"));
        } else {
            return recordRepository.findRecordByChatId(chatId).getRecordId();
        }
        return null;
    }
    /**
     * метод сохраняет в базе данных часть отчета отвечающую за информацию  <br>
     * об измеении в поведении питомца <br>
     * see ChangeInBehavior
     * @param update
     */

    public void saveRecord (Update update) {
        logger.info("Процесс сохранения отчета");
        LocalDateTime localDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        String record = update.message().text();
        long chatId = update.message().chat().id();
        int dietIndex = record.indexOf("Диета:");
        int adaptationIndex = record.indexOf("Адаптация:");
        int behaviorIndex = record.indexOf("Изменение в поведении:");

        String dietResult = record.substring(dietIndex, adaptationIndex);
        String adaptationResult = record.substring(adaptationIndex, behaviorIndex);
        String behaviorResult = record.substring(behaviorIndex, record.length());


        if (dietResult.length() < 8){
            telegramBot.execute(new SendMessage(chatId,RECORD_DIETA.getMessage()));
        }
        else if(adaptationResult.length() < 8){
            telegramBot.execute(new SendMessage(chatId, RECORD_ADAPTATION.getMessage()));
        }
        else if(behaviorResult.length()< 8) {
            telegramBot.execute(new SendMessage(chatId,RECORD_BEHAVIOR.getMessage()));
        } else {
            Record recordForBase = new Record();
            recordForBase.setDiet(dietResult);
            recordForBase.setAdaptation(adaptationResult);
            recordForBase.setChangeInBehavior(behaviorResult);
            recordForBase.setChatId(chatId);
            recordForBase.setDateTime(localDate);
            recordRepository.save(recordForBase);
            telegramBot.execute(new SendMessage(chatId, SAVE_INFORMATION.getMessage()));
            logger.info("текстовый отчет занесен в базу данных");
        }
    }

}
