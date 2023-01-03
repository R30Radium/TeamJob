package telegram.teamjob.handler.button_answers.dogButton;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.constants.BotMessageEnum;
import telegram.teamjob.entity.InformationForOwner;
import telegram.teamjob.implementation.ShelterServiceImpl;
import telegram.teamjob.implementation.TelegramBotUpdatesListener;
import telegram.teamjob.repositories.InformationForOwnerRepository;

import java.util.List;

import static telegram.teamjob.constants.BotButtonForShelterMenuEnum.COMMAND_SAFE_CONTACT_DETAILS_FOR_COMMUNICATION;
import static telegram.teamjob.constants.BotMessageEnum.*;
import static telegram.teamjob.constants.Cat.TextForResponseFromMenuButtonsDog.*;

@Service
public class DogButtonAnswers {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final ShelterServiceImpl shelterService;
    private final InformationForOwnerRepository informationForOwnerRepository;

    public DogButtonAnswers(TelegramBot telegramBot, ShelterServiceImpl shelterService, InformationForOwnerRepository informationForOwnerRepository) {
        this.telegramBot = telegramBot;
        this.shelterService = shelterService;
        this.informationForOwnerRepository = informationForOwnerRepository;
    }
    /**
     * метод возвращает пользователю один из подразделов главного меню (в зависимости <br>
     * от того, какой раздел главного меню выбрал пользователь)
     *
     * @param update
     */
    public void checkButtonAnswerDogs(Update update) {
        String callBackData = update.callbackQuery().data();
        logger.info(callBackData);
        long chatId = update.callbackQuery().message().chat().id();
        switch (callBackData) {
            case "Узнать информацию о приюте":
                //вызов  меню этапа 1
                String newMessage = USER_CHOOSE_SHELTER_INFO.getMessage();
                //  EditMessageText messageText = new EditMessageText(chatId, messageId, newMessage);
                telegramBot.execute(new SendMessage(chatId, newMessage)
                        .replyMarkup(shelterService.makeButtonsForMenuStageOne()));
                break;
            case "Как взять собаку из приюта":
                //вызов  меню этапа 2
                String message = USER_CHOOSE_DOG_INSTRUCTION.getMessage();
                telegramBot.execute(new SendMessage(chatId, message)
                        .replyMarkup(shelterService.makeButtonsForMenuStageTwo()));
                break;
            case "Прислать отчет о питомце":
                //Отчет по питомцу
                String creatRecordMessage = BotMessageEnum.DAILY_RECORD_INFO.getMessage();
                telegramBot.execute(new SendMessage(chatId, creatRecordMessage));
                logger.info("Message Sent" + " Method - sendRecord");
                break;
            case "Позвать волонтера":
                telegramBot.execute(new SendMessage(chatId, ASK_HELP.getMessage()));
                break;
        }
    }

    public void sendResponseForFirstAndSecondMenuDogs(Update update) {
        logger.info("вызвал метод sendAnswer");
        String answerMenu = update.callbackQuery().data();
        long chatId = update.callbackQuery().message().chat().id();
        int messageId = update.callbackQuery().message().messageId();
        logger.info("Ответ от кнопки " + answerMenu);
        // List<Shelter> shelters = shelterRepository.findAll();
        List<InformationForOwner> info = informationForOwnerRepository.findAll();
        switch (answerMenu) {
            case "info":
                    SendMessage answer = new SendMessage(chatId, INFORMATION_ABOUT_SHELTER_DOG.getMessage());
                    logger.warn("IMPORTANT information about shelter dog");
                    telegramBot.execute(answer);
                break;
            case "workTime":
//                        String workSchedule = shelter.getWorkScheduleShelter();
                    SendMessage answer2 = new SendMessage(chatId, WORK_TIME_SHELTER_DOG.getMessage());
                    logger.warn("IMPORTANT work time shelter dog");
                    telegramBot.execute(answer2);
                break;
            case "address":
//                    String address = shelter.getAddressShelter();
                    SendMessage answer3 = new SendMessage(chatId, ADDRESS_SHELTER_DOG.getMessage());
                    logger.warn("IMPORTANT  address shelter dog");
                    telegramBot.execute(answer3);
                break;
            case "way":

//                        String way = shelter.getDrivingDirectionsShelter();
                    SendMessage answer4 = new SendMessage(chatId,  WAY_SHELTER_DOG.getMessage());
                    logger.warn("IMPORTANT way shelter");
                    telegramBot.execute(answer4);
                break;
            case "safety":
//                        String safety = shelter.getSafetyAtShelter();
                    SendMessage answer5 = new SendMessage(chatId,SAFETY_DOG.getMessage());
                    logger.warn("IMPORTANT safety shelter");
                    telegramBot.execute(answer5);
                break;
            case "volunteer":
                telegramBot.execute(new SendMessage(chatId,  ASK_HELP.getMessage()));
                break;
            case "contact":
                telegramBot.execute(new SendMessage(chatId, COMMAND_SAFE_CONTACT_DETAILS_FOR_COMMUNICATION.getText()));
                break;
            case "rules":
//                        String information = infoOwner.getRules();
                    SendMessage answer6 = new SendMessage(chatId,  RULES_FOR_DATING_DOG.getMessage());
                    logger.warn("IMPORTANT rules");
                    telegramBot.execute(answer6);
                break;
            case "docs":
                    SendMessage answer7 = new SendMessage(chatId, LIST_DOCUMENTS_FOR_DOG.getMessage());
                    logger.warn("IMPORTANT about documents");
                    telegramBot.execute(answer7);
                break;
            case "transportation":
//                        String information = infoOwner.getTranspartation();
                    SendMessage answer8 = new SendMessage(chatId, RECOMMENDATIONS_FOR_TRANSPORTATION_DOG.getMessage());
                    logger.warn("IMPORTANT transportation dogs");
                    telegramBot.execute(answer8);
                break;
            case "arrangementPuppy":
                    SendMessage answer9 = new SendMessage(chatId, RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_PUPPY.getMessage());
                    logger.warn("IMPORTANT arrangement puppy");
                    telegramBot.execute(answer9);
                break;
            case "arrangementDog":
                    SendMessage answer10 = new SendMessage(chatId, RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_ADULT_DOG.getMessage());
                    logger.warn("IMPORTANT arrangement dog");
                    telegramBot.execute(answer10);
                break;
            case "arrangementDogInvalid":
                    SendMessage answer11 = new SendMessage(chatId, RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_DISABLED_DOG.getMessage());
                    logger.warn("IMPORTANT arrangement dog invalid");
                    telegramBot.execute(answer11);
                break;
            case "cynologist":
                    SendMessage  answer12 = new SendMessage(chatId, RECOMMENDATIONS_OF_DOG_CYNOLOGISTS.getMessage());
                    logger.warn("IMPORTANT cynologist");
                    telegramBot.execute(answer12);
                break;
            case "goodCynologists":
                    SendMessage answer13 = new SendMessage(chatId,  GOOD_CYNOLOGISTS.getMessage());
                    logger.warn("IMPORTANT good cynologists");
                    telegramBot.execute(answer13);
                break;
            case "reject":
                    SendMessage answer14 = new SendMessage(chatId, REJECTS_DOGS.getMessage());
                    logger.warn("IMPORTANT  reject");
                    telegramBot.execute(answer14);
                break;
        }
    }
}
