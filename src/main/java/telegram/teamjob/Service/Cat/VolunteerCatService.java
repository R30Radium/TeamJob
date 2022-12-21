package telegram.teamjob.Service.Cat;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public interface VolunteerCatService {

    InlineKeyboardMarkup userCatStatusMenuButtons();
    InlineKeyboardMarkup menuForVolunteerCat();
    void sendMessageForAdopterCat(long chatId);
}
