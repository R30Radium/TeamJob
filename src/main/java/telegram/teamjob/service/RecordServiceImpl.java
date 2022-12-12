package telegram.teamjob.Service;


import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.implementation.RecordService;
import telegram.teamjob.entity.User;
import telegram.teamjob.repositories.RecordRepository;
import telegram.teamjob.repositories.UserRepository;
import telegram.teamjob.entity.Record;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;

@Service
public class RecordServiceImpl implements RecordService {
    private Logger logger = LoggerFactory.getLogger(RecordServiceImpl.class);
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

    public RecordServiceImpl(RecordRepository recordRepository, UserRepository userRepository) {
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
    }
    @Override
    public Record getStringRecord(Update update) {
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
    @Override
    public void createRecord(Update update) {
        logger.info("Processing create Record");
        recordRepository.save(getStringRecord(update));
    }
    @Override
    public Long findRecordId(Update update) {
        Long chatId = update.message().chat().id();
        LinkedList<Record> recordsList = recordRepository.findAllByChatId(chatId);
        Record record = recordsList.peekLast();
        if (record != null) {
            return record.getRecordId();
        } else if (recordRepository.findAll().isEmpty()) {
            return null;
        } else {
            return recordRepository.findRecordByChatId(chatId).getRecordId();
        }
    }
}

