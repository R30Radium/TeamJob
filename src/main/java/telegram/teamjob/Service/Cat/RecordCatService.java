package telegram.teamjob.Service.Cat;

import com.pengrad.telegrambot.model.Update;


public interface RecordCatService {

    Long findRecordCatId(Update update);

    void saveRecordCat(Update update);
}
