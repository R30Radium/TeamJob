package telegram.teamjob.handler;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;
import telegram.teamjob.handler.button_answers.catButton.CatButtonAnswers;
import telegram.teamjob.handler.button_answers.dogButton.DogButtonAnswers;

import static telegram.teamjob.constants.BotButtonEnum.*;
import static telegram.teamjob.constants.Cat.BotButtonForCat.*;
@Service
public class DataCheck {
    private final DogButtonAnswers dogButtonAnswers;
    private final CatButtonAnswers catButtonAnswers;

    public DataCheck(DogButtonAnswers dogButtonAnswers, CatButtonAnswers catButtonAnswers) {
        this.dogButtonAnswers = dogButtonAnswers;
        this.catButtonAnswers = catButtonAnswers;
    }

    public void checkDogButtonAnswer(Update update) {
        if (update.callbackQuery().data().equals(BUTTON_INFO.getMessage()) ||
                update.callbackQuery().data().equals(BUTTON_INSTRUCTION_DOG.getMessage()) ||
                update.callbackQuery().data().equals(BUTTON_RECORD.getMessage()) ||
                update.callbackQuery().data().equals(BUTTON_HELP.getMessage())) {
            dogButtonAnswers.checkButtonAnswerDogs(update);
        } else if (update.callbackQuery().data().equals("info") || //команды для меню первого этапа
                update.callbackQuery().data().equals("way") ||
                update.callbackQuery().data().equals("address") ||
                update.callbackQuery().data().equals("safety") ||
                update.callbackQuery().data().equals("volunteer") ||
                update.callbackQuery().data().equals("workTime") ||
                update.callbackQuery().data().equals("contact") ||
                update.callbackQuery().data().equals("rules") ||
                update.callbackQuery().data().equals("docs") ||
                update.callbackQuery().data().equals("transportation") ||
                update.callbackQuery().data().equals("arrangementPuppy") ||
                update.callbackQuery().data().equals("arrangementDog") ||
                update.callbackQuery().data().equals("arrangementDogInvalid") ||
                update.callbackQuery().data().equals("cynologist") ||
                update.callbackQuery().data().equals("goodCynologists") ||
                update.callbackQuery().data().equals("reject")) {
            dogButtonAnswers.sendResponseForFirstAndSecondMenuDogs(update);
        }
    }

    public void checkCatButtonAnswer(Update update) {
        if (update.callbackQuery().data().equals(BUTTON_INFO_CAT.getMessage()) ||
                update.callbackQuery().data().equals(BUTTON_INSTRUCTION_CAT.getMessage()) ||
                update.callbackQuery().data().equals(BUTTON_RECORD_CAT.getMessage()) ||
                update.callbackQuery().data().equals(BUTTON_HELP_CAT.getMessage())) {
            catButtonAnswers.checkButtonAnswerCats(update);
        } else if (update.callbackQuery().data().equals("infoCat") || //команды для меню первого этапа
                update.callbackQuery().data().equals("wayCat") ||
                update.callbackQuery().data().equals("addressCat") ||
                update.callbackQuery().data().equals("safetyCat") ||
                update.callbackQuery().data().equals("volunteerCat") ||
                update.callbackQuery().data().equals("workTimeCat") ||
                update.callbackQuery().data().equals("contact") ||
                update.callbackQuery().data().equals("rulesCat") ||
                update.callbackQuery().data().equals("docsCat") ||
                update.callbackQuery().data().equals("transportationCat") ||
                update.callbackQuery().data().equals("arrangementKitty") ||
                update.callbackQuery().data().equals("arrangementCat") ||
                update.callbackQuery().data().equals("arrangementCatDisabled") ||
                update.callbackQuery().data().equals("rejectCat")) {
            catButtonAnswers.sendResponseForFirstAndSecondMenuCats(update);
        }
    }


}
