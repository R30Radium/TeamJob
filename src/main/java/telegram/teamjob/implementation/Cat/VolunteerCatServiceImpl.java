package telegram.teamjob.implementation.Cat;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.Service.Cat.VolunteerCatService;
import telegram.teamjob.implementation.VolunteerServiceImpl;
import telegram.teamjob.repositories.Cat.ContactCatRepository;
import telegram.teamjob.repositories.Cat.VolunteerCatRepository;
import telegram.teamjob.repositories.ContactRepository;
import telegram.teamjob.repositories.VolunteerRepository;

import static telegram.teamjob.constants.BotButtonEnum.*;
import static telegram.teamjob.constants.BotButtonEnum.BUTTON_CHECK_REPORTS;
import static telegram.teamjob.constants.BotMessageVolunteer.MESSAGE_FOR_ADOPTER;

@Service
public class VolunteerCatServiceImpl implements VolunteerCatService {
    private final Logger logger = LoggerFactory.getLogger(VolunteerCatServiceImpl.class);

    private TelegramBot telegramBot;
    private VolunteerCatRepository volunteerCatRepository;

    private final ContactCatRepository contactCatRepository;

    public VolunteerCatServiceImpl(TelegramBot telegramBot, VolunteerCatRepository volunteerCatRepository,
                                ContactCatRepository contactCatRepository) {
        this.telegramBot = telegramBot;
        this.volunteerCatRepository = volunteerCatRepository;
        this.contactCatRepository = contactCatRepository;
    }

    @Override
    public InlineKeyboardMarkup userCatStatusMenuButtons() {
        InlineKeyboardMarkup markupCat = new InlineKeyboardMarkup();
        var buttonGuest = new InlineKeyboardButton(BUTTON_GUEST.getMessage());
        var buttonEmployee = new InlineKeyboardButton(BUTTON_EMPLOYEE.getMessage());

        buttonGuest.callbackData(BUTTON_GUEST.getMessage());
        buttonEmployee.callbackData(BUTTON_EMPLOYEE.getMessage());

        markupCat.addRow(buttonGuest);
        markupCat.addRow(buttonEmployee);
        return markupCat;
    }

    @Override
    public InlineKeyboardMarkup menuForVolunteerCat() {
        InlineKeyboardMarkup markupCat2 = new InlineKeyboardMarkup();
        var buttonCheckContacts = new InlineKeyboardButton(BUTTON_CHECK_CONTACT.getMessage());
        var buttonAddUser = new InlineKeyboardButton(BUTTON_ADD_USER.getMessage());
        var buttonCheckReports = new InlineKeyboardButton(BUTTON_CHECK_REPORTS.getMessage());
        //   var buttonSendWarningAboutBadReport = new InlineKeyboardButton(BUTTON_SEND_WARNING_ABOUT_BED_REPORT.getMessage());
        //  var buttonMakeDecisionOnProbation = new InlineKeyboardButton(BUTTON_MAKE_DECISION_ON_PROBATION.getMessage());

        buttonCheckContacts.callbackData(BUTTON_CHECK_CONTACT.getMessage());
        buttonAddUser.callbackData(BUTTON_ADD_USER.getMessage());
        buttonCheckReports.callbackData(BUTTON_CHECK_REPORTS.getMessage());
        //  buttonSendWarningAboutBadReport.callbackData(BUTTON_SEND_WARNING_ABOUT_BED_REPORT.getMessage());
        // buttonMakeDecisionOnProbation.callbackData(BUTTON_MAKE_DECISION_ON_PROBATION.getMessage());

        markupCat2.addRow(buttonCheckContacts);
        markupCat2.addRow(buttonAddUser);
        markupCat2.addRow(buttonCheckReports);
        //  markup2.addRow(buttonSendWarningAboutBadReport);
        //   markup2.addRow(buttonMakeDecisionOnProbation);

        return markupCat2;
    }
    @Override
    public void sendMessageForAdopterCat(long chatId){
        telegramBot.execute(new SendMessage(chatId, MESSAGE_FOR_ADOPTER.getMessage()));
    }


}
