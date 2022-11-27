package telegram.teamjob.Service;

import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.entity.Record;
import telegram.teamjob.entity.User;
import telegram.teamjob.repository.RecordRepository;
import telegram.teamjob.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

@Service
public class RecordService {
    private Logger logger = LoggerFactory.getLogger(RecordService.class);
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

    public RecordService(RecordRepository recordRepository, UserRepository userRepository) {
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
    }

    private Record getStringRecord(Update update) {
        Long chatId = update.message().chat().id();
        String message = update.message().text();
        LocalDateTime localDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        Record newRecord = new Record();
        User user = userRepository.findByChatId(chatId);
        newRecord.setLifeRecord(message);
        newRecord.setDateTime(localDate);
        newRecord.setChatId(chatId);
        newRecord.setUser(user);
        return newRecord;
    }

    public void createRecord(Update update) {
        logger.info("Processing create Record");
        recordRepository.save(getStringRecord(update));
    }

    public Long findRecordId(Update update) {
        Long chatId = update.message().chat().id();
        LinkedList<Record> recordsList = recordRepository.findAllByChatId(chatId);
        Record record = recordsList.peekLast();
        if (record != null) {
            return record.getRecordId();
        } else {
            //Добавить условие (Если есть в БД)
            return recordRepository.findRecordByChatId(chatId).getRecordId();
        }
    }
}
