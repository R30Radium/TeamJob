package telegram.teamjob.service;


import com.pengrad.telegrambot.model.Update;
import telegram.teamjob.entity.Record;

public interface RecordService {

    // Record getStringRecord(Update update);
    //  void createRecord(Update update);
    Long findRecordId(Update update);

    void saveRecord(Update update);


}