package telegram.teamjob.Service.Cat;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public interface ShelterForCatService {

    InlineKeyboardMarkup ourMenuButtonsCat();
    InlineKeyboardMarkup makeButtonsForMenuStageOneCat();

    InlineKeyboardMarkup makeButtonsForMenuStageTwoCat();
}
