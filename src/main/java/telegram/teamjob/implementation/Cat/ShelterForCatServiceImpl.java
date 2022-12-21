package telegram.teamjob.implementation.Cat;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.Service.Cat.ShelterForCatService;
import telegram.teamjob.repositories.Cat.ShelterForCatRepository;


import static telegram.teamjob.constants.Cat.BotButtonForCat.*;
import static telegram.teamjob.constants.BotButtonForShelterMenuEnum.*;
import static telegram.teamjob.constants.BotButtonForShelterMenuEnum.COMMAND_CALL_VOLUNTEER;

@Service
public class ShelterForCatServiceImpl implements ShelterForCatService {


    private TelegramBot telegramBot;
    private final  ShelterForCatRepository shelterForCatRepository;

    private Logger logger = LoggerFactory.getLogger(ShelterForCatServiceImpl.class);

    public ShelterForCatServiceImpl(TelegramBot telegramBot, ShelterForCatRepository  shelterForCatRepository){
        this.telegramBot = telegramBot;
        this.shelterForCatRepository = shelterForCatRepository;
    }
    @Override
    public InlineKeyboardMarkup ourMenuButtonsCat() {
        InlineKeyboardMarkup markupCat = new InlineKeyboardMarkup();
        var button1Info = new InlineKeyboardButton(BUTTON_INFO_CAT.getMessage());
        var button2Instruction = new InlineKeyboardButton(BUTTON_INSTRUCTION_CAT.getMessage());
        var button3Record = new InlineKeyboardButton(BUTTON_RECORD_CAT.getMessage());
        var button4Help = new InlineKeyboardButton(BUTTON_HELP_CAT.getMessage());

        button1Info.callbackData(BUTTON_INFO_CAT.getMessage());
        button2Instruction.callbackData(BUTTON_INSTRUCTION_CAT.getMessage());
        button3Record.callbackData(BUTTON_RECORD_CAT.getMessage());
        button4Help.callbackData(BUTTON_HELP_CAT.getMessage());

        /* Данные кнопки будут располагаться друг под другом. Можно использоваться в линию
           для этого необходимые кнопки можно перечислить в параметрах markup.addRow(b1, b2, b3, b4);
           Тогда информация будет сжата по размеру кнопки
        */
        markupCat.addRow(button1Info);
        markupCat.addRow(button2Instruction);
        markupCat.addRow(button3Record);
        markupCat.addRow(button4Help);
        logger.info("Меню отправлено");
        return markupCat;
    }
    @Override
    public InlineKeyboardMarkup makeButtonsForMenuStageOneCat() {
        InlineKeyboardMarkup markupCat = new InlineKeyboardMarkup();
        var buttonAboutShelter  = new InlineKeyboardButton(COMMAND_INFORMATION_ABOUT_SHELTER.getText());
        var  buttonWorkTime = new InlineKeyboardButton(COMMAND_WORK_SCHEDULE_SHELTER.getText());
        var  buttonAddress = new InlineKeyboardButton(COMMAND_ADDRESS_SHELTER.getText());
        var   buttonWay = new InlineKeyboardButton(COMMAND_DRIVING_DIRECTIONS.getText());
        var   buttonSecurityContact = new InlineKeyboardButton(COMMAND_SECURITY_CONTACT_CAT.getMessage());
        var buttonSafety = new InlineKeyboardButton(COMMAND_SAFETY_SHELTER.getText());
        var buttonContact = new InlineKeyboardButton(COMMAND_LEAVE_DATA_FOR_COMMUNICATION.getText());
        var buttonVolunteer = new InlineKeyboardButton(COMMAND_CALL_VOLUNTEER.getText());

        buttonAboutShelter.callbackData("infoCat");
        buttonWorkTime.callbackData("workTimeCat");
        buttonAddress.callbackData("addressCat");
        buttonWay.callbackData("wayCat");
        buttonSecurityContact.callbackData("SecurityContactCat");
        buttonSafety.callbackData("safetyCat");
        buttonContact.callbackData("contactCat");
        buttonVolunteer.callbackData("volunteerCat");

        markupCat.addRow(buttonAboutShelter);
        markupCat.addRow(buttonWorkTime);
        markupCat.addRow(buttonAddress);
        markupCat.addRow(buttonWay);
        markupCat.addRow(buttonSecurityContact);
        markupCat.addRow(buttonSafety);
        markupCat.addRow(buttonContact);
        markupCat.addRow(buttonVolunteer);
        return markupCat;
    }

    public InlineKeyboardMarkup makeButtonsForMenuStageTwoCat() {
        InlineKeyboardMarkup markupCat = new InlineKeyboardMarkup();

        var button1Rules = new InlineKeyboardButton(BUTTON_KNOW_CAT.getMessage());
        var button2Docs = new InlineKeyboardButton( BUTTON_DOC_CAT.getMessage());
        var button3Transportation = new InlineKeyboardButton(BUTTON_TRANSPORTATION_CAT.getMessage());
        var button4ArrangementKitty = new InlineKeyboardButton( BUTTON_KITTY.getMessage());
        var button5ArrangementCat = new InlineKeyboardButton(BUTTON_ARRANGEMENT_CAT.getMessage());
        var button6ArrangementCatInvalid = new InlineKeyboardButton(BUTTON_ARRANGEMENT_CAT_INVALID.getMessage());
        var button7Reject = new InlineKeyboardButton(BUTTON_REJECT_CAT.getMessage());
        var button8Contact = new InlineKeyboardButton(BUTTON_CONTACT_CAT.getMessage());
        var button9Volunteer = new InlineKeyboardButton(BUTTON_HELP_CAT.getMessage());

        button1Rules.callbackData("rulesCat");
        button2Docs.callbackData("docsCat");
        button3Transportation.callbackData("transpartationCat");
        button4ArrangementKitty.callbackData("arrangementKitty");
        button5ArrangementCat.callbackData("arrangementCat");
        button6ArrangementCatInvalid.callbackData("arrangementCatInvalid");
        button7Reject.callbackData("rejectCat");
        button8Contact.callbackData("contactCat");
        button9Volunteer.callbackData("volunteerCat");

        markupCat.addRow(button1Rules);
        markupCat.addRow(button2Docs);
        markupCat.addRow(button3Transportation);
        markupCat.addRow(button4ArrangementKitty);
        markupCat.addRow(button5ArrangementCat);
        markupCat.addRow(button6ArrangementCatInvalid);
        markupCat.addRow(button7Reject);
        markupCat.addRow(button8Contact);
        markupCat.addRow(button9Volunteer);
        return markupCat;
    }


}
