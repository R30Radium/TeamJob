package telegram.teamjob.implementation; // todo: почему пакет называется implementation, а внутри наоборот НЕ реализации, а интерфейсы?

import com.pengrad.telegrambot.model.Update;
import telegram.teamjob.entity.User;


public interface UserService {

    User getStringUser(Update update);
    User createUser(Update update);
    User getUser(Long chatId);
}
