package telegram.teamjob.service;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public interface ShelterService {

    InlineKeyboardMarkup ourMenuButtons();
    InlineKeyboardMarkup makeButtonsForMenuStageOne();

    InlineKeyboardMarkup makeButtonsForMenuStageTwo();
}
