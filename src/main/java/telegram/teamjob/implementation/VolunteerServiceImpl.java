package telegram.teamjob.implementation;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.repositories.ContactRepository;
import telegram.teamjob.repositories.VolunteerRepository;
import telegram.teamjob.Service.VolunteerService;

import static telegram.teamjob.constants.BotButtonEnum.*;
import static telegram.teamjob.constants.BotMessageVolunteer.MESSAGE_FOR_ADOPTER;
@Service
public class VolunteerServiceImpl implements VolunteerService {

    private final Logger logger = LoggerFactory.getLogger(VolunteerServiceImpl.class);

    private TelegramBot telegramBot;
    private VolunteerRepository volunteerRepository;

    private final ContactRepository contactRepository;

    public VolunteerServiceImpl(TelegramBot telegramBot, VolunteerRepository volunteerRepository,
                                ContactRepository contactRepository) {
        this.telegramBot = telegramBot;
        this.volunteerRepository = volunteerRepository;
        this.contactRepository = contactRepository;
    }

    @Override
    public InlineKeyboardMarkup userStatusMenuButtons() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        var buttonGuest = new InlineKeyboardButton(BUTTON_GUEST.getMessage());
        var buttonEmployee = new InlineKeyboardButton(BUTTON_EMPLOYEE.getMessage());

        buttonGuest.callbackData(BUTTON_GUEST.getMessage());
        buttonEmployee.callbackData(BUTTON_EMPLOYEE.getMessage());

        markup.addRow(buttonGuest);
        markup.addRow(buttonEmployee);
        return markup;
    }

    @Override
    public InlineKeyboardMarkup menuForVolunteer() {
        InlineKeyboardMarkup markup2 = new InlineKeyboardMarkup();
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

        markup2.addRow(buttonCheckContacts);
        markup2.addRow(buttonAddUser);
        markup2.addRow(buttonCheckReports);
        //  markup2.addRow(buttonSendWarningAboutBadReport);
        //   markup2.addRow(buttonMakeDecisionOnProbation);

        return markup2;
    }
    @Override
    public void sendMessageForAdopter(long chatId){
        telegramBot.execute(new SendMessage(chatId, MESSAGE_FOR_ADOPTER.getMessage()));
    }

}



