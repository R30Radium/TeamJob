package telegram.teamjob.Impl;

import com.pengrad.telegrambot.model.Update;
import telegram.teamjob.Entity.User;


public interface UserImpl {

    User getStringUser(Update update);
    User createUser(Update update);
    User getUser(Long chatId);
}
