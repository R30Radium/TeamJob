package telegram.teamjob.Service;


import com.pengrad.telegrambot.model.Update;
import telegram.teamjob.entity.Record;

public interface RecordService {

    Long findRecordId(Update update);

    void saveRecord(Update update);


}