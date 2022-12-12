package telegram.teamjob.service;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public interface VolunteerService {

    InlineKeyboardMarkup userStatusMenuButtons();
    InlineKeyboardMarkup menuForVolunteer();
    void sendMessageForAdopter(long chatId);
}
