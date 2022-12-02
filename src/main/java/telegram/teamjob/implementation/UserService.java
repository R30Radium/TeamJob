package telegram.teamjob.implementation;

import com.pengrad.telegrambot.model.Update;
import telegram.teamjob.entity.User;


public interface UserService {

    User getStringUser(Update update);
    User createUser(Update update);
    User getUser(Long chatId);
}
