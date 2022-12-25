package telegram.teamjob.implementation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.repositories.ShelterRepository;
import telegram.teamjob.Service.ShelterService;

import static telegram.teamjob.constants.BotButtonEnum.*;
import static telegram.teamjob.constants.BotButtonForShelterMenuEnum.*;
import static telegram.teamjob.constants.Cat.BotButtonForCat.*;

@Service
public class ShelterServiceImpl implements ShelterService {
    private TelegramBot telegramBot;
    private final ShelterRepository shelterRepository;

    private Logger logger = LoggerFactory.getLogger(ShelterServiceImpl.class);

    public ShelterServiceImpl(TelegramBot telegramBot,ShelterRepository shelterRepository){
        this.telegramBot = telegramBot;
        this.shelterRepository = shelterRepository;
    }
    @Override
    public InlineKeyboardMarkup ourMenuButtons() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        var button1Info = new InlineKeyboardButton(BUTTON_INFO.getMessage());
        var button2Instruction = new InlineKeyboardButton(BUTTON_INSTRUCTION_DOG.getMessage());
        var button3Record = new InlineKeyboardButton(BUTTON_RECORD.getMessage());
        var button4Help = new InlineKeyboardButton(BUTTON_HELP.getMessage());

        button1Info.callbackData(BUTTON_INFO.getMessage());
        button2Instruction.callbackData(BUTTON_INSTRUCTION_DOG.getMessage());
        button3Record.callbackData(BUTTON_RECORD.getMessage());
        button4Help.callbackData(BUTTON_HELP.getMessage());

        /* Данные кнопки будут располагаться друг под другом. Можно использоваться в линию
           для этого необходимые кнопки можно перечислить в параметрах markup.addRow(b1, b2, b3, b4);
           Тогда информация будет сжата по размеру кнопки
        */
        markup.addRow(button1Info);
        markup.addRow(button2Instruction);
        markup.addRow(button3Record);
        markup.addRow(button4Help);
        logger.info("Меню отправлено");
        return markup;
    }
    @Override
    public InlineKeyboardMarkup makeButtonsForMenuStageOne() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        var buttonAboutShelter  = new InlineKeyboardButton(COMMAND_INFORMATION_ABOUT_SHELTER.getText());
        var  buttonWorkTime = new InlineKeyboardButton(COMMAND_WORK_SCHEDULE_SHELTER.getText());
        var  buttonAddress = new InlineKeyboardButton(COMMAND_ADDRESS_SHELTER.getText());
        var   buttonWay = new InlineKeyboardButton(COMMAND_DRIVING_DIRECTIONS.getText());
        var buttonSafety = new InlineKeyboardButton(COMMAND_SAFETY_SHELTER.getText());
        var buttonContact = new InlineKeyboardButton(COMMAND_LEAVE_DATA_FOR_COMMUNICATION.getText());
        var buttonVolunteer = new InlineKeyboardButton(COMMAND_CALL_VOLUNTEER.getText());

        buttonAboutShelter.callbackData("info");
        buttonWorkTime.callbackData("workTime");
        buttonAddress.callbackData("address");
        buttonWay.callbackData("way");
        buttonSafety.callbackData("safety");
        buttonContact.callbackData("contact");
        buttonVolunteer.callbackData("volunteer");

        markup.addRow(buttonAboutShelter);
        markup.addRow(buttonWorkTime);
        markup.addRow(buttonAddress);
        markup.addRow(buttonWay);
        markup.addRow(buttonSafety);
        markup.addRow(buttonContact);
        markup.addRow(buttonVolunteer);
        return markup;
    }

    public InlineKeyboardMarkup makeButtonsForMenuStageTwo() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        var button1Rules = new InlineKeyboardButton(BUTTON_KNOW.getMessage());
        var button2Docs = new InlineKeyboardButton(BUTTON_DOC.getMessage());
        var button3Transportation = new InlineKeyboardButton(BUTTON_TRANSPORTATION.getMessage());
        var button4ArrangementPuppy = new InlineKeyboardButton(BUTTON_PUPPY.getMessage());
        var button5ArrangementDog = new InlineKeyboardButton(BUTTON_ARRANGEMENT_DOG.getMessage());
        var button6ArrangementDogInvalid = new InlineKeyboardButton(BUTTON_ARRANGEMENT_DOG_INVALID.getMessage());
        var button7Cynologist = new InlineKeyboardButton(BUTTON_CYNOLOGIST.getMessage());
        var button8GoodCynologist = new InlineKeyboardButton(BUTTON_GOOD_CYNOLOGIST.getMessage());
        var button9Reject = new InlineKeyboardButton(BUTTON_REJECT.getMessage());
        var button10Contact = new InlineKeyboardButton(BUTTON_CONTACT.getMessage());
        var button11Volunteer = new InlineKeyboardButton(BUTTON_HELP.getMessage());

        button1Rules.callbackData("rules");
        button2Docs.callbackData("docs");
        button3Transportation.callbackData("transpartation");
        button4ArrangementPuppy.callbackData("arrangementPuppy");
        button5ArrangementDog.callbackData("arrangementDog");
        button6ArrangementDogInvalid.callbackData("arrangementDogInvalid");
        button7Cynologist.callbackData("cynologist");
        button8GoodCynologist.callbackData("goodCynologists");
        button9Reject.callbackData("reject");
        button10Contact.callbackData("contact");
        button11Volunteer.callbackData("volunteer");

        markup.addRow(button1Rules);
        markup.addRow(button2Docs);
        markup.addRow(button3Transportation);
        markup.addRow(button4ArrangementPuppy);
        markup.addRow(button5ArrangementDog);
        markup.addRow(button6ArrangementDogInvalid);
        markup.addRow(button7Cynologist);
        markup.addRow(button8GoodCynologist);
        markup.addRow(button9Reject);
        markup.addRow(button10Contact);
        markup.addRow(button11Volunteer);
        return markup;
    }

    public InlineKeyboardMarkup ourMenuCatButtons() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        var button1Info = new InlineKeyboardButton(BUTTON_INFO.getMessage());
        var button2Instruction = new InlineKeyboardButton(BUTTON_INSTRUCTION_CAT.getMessage());
        var button3Record = new InlineKeyboardButton(BUTTON_RECORD.getMessage());
        var button4Help = new InlineKeyboardButton(BUTTON_HELP.getMessage());

        button1Info.callbackData(BUTTON_INFO.getMessage());
        button2Instruction.callbackData(BUTTON_INSTRUCTION_CAT.getMessage());
        button3Record.callbackData(BUTTON_RECORD.getMessage());
        button4Help.callbackData(BUTTON_HELP.getMessage());

        /* Данные кнопки будут располагаться друг под другом. Можно использоваться в линию
           для этого необходимые кнопки можно перечислить в параметрах markup.addRow(b1, b2, b3, b4);
           Тогда информация будет сжата по размеру кнопки
        */
        markup.addRow(button1Info);
        markup.addRow(button2Instruction);
        markup.addRow(button3Record);
        markup.addRow(button4Help);
        logger.info("Меню отправлено");
        return markup;
    }





}

