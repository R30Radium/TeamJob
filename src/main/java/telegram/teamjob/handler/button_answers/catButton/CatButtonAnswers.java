package telegram.teamjob.handler.button_answers.catButton;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.constants.BotMessageEnum;
import telegram.teamjob.implementation.Cat.ShelterForCatServiceImpl;
import telegram.teamjob.implementation.TelegramBotUpdatesListener;

import static telegram.teamjob.constants.BotButtonForShelterMenuEnum.COMMAND_SAFE_CONTACT_DETAILS_FOR_COMMUNICATION;
import static telegram.teamjob.constants.BotMessageEnum.*;
import static telegram.teamjob.constants.Cat.BotMessageEnumCat.*;
import static telegram.teamjob.constants.Cat.TextForResponseFromMenuButtonsCat.*;
@Service
public class CatButtonAnswers {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final ShelterForCatServiceImpl shelterForCatService;


    public CatButtonAnswers(TelegramBot telegramBot, ShelterForCatServiceImpl shelterForCatService) {
        this.telegramBot = telegramBot;
        this.shelterForCatService = shelterForCatService;
    }
    public void checkButtonAnswerCats(Update update) {

        String callBackData = update.callbackQuery().data();
        logger.info(callBackData);
        long chatId = update.callbackQuery().message().chat().id();
        switch (callBackData) {
            case "Узнать информацию о приюте":
                //вызов  меню этапа 1
                String newMessage = USER_CHOOSE_SHELTER_INFO.getMessage();
                //  EditMessageText messageText = new EditMessageText(chatId, messageId, newMessage);
                telegramBot.execute(new SendMessage(chatId, newMessage)
                        .replyMarkup(shelterForCatService.makeButtonsForMenuStageOneCat()));
                break;
            case "Как взять кошку из приюта":
                //вызов  меню этапа 2
                String message = ANSWER_FOR_MENU_INFORMATION_CAT.getMessage();
                telegramBot.execute(new SendMessage(chatId, message)
                        .replyMarkup(shelterForCatService.makeButtonsForMenuStageTwoCat()));
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

    public void sendResponseForFirstAndSecondMenuCats(Update update) {
        logger.info("вызвал метод sendResponseForFirstAndSecondMenuCats");
        String answerMenu = update.callbackQuery().data();
        long chatId = update.callbackQuery().message().chat().id();
        int messageId = update.callbackQuery().message().messageId();
        logger.info("Ответ от кнопки " + answerMenu);
        switch (answerMenu) {
            case "info":
                logger.info("case");
                try {
                    logger.info("try case 1");
                    String information = "Информация о приюте";
                    EditMessageText answer = new EditMessageText(chatId, messageId, information);
                    logger.warn("IMPORTANT" + information);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                break;
            case "workTime":
                try {
                    String workSchedule = "24/7 без перерывов";
                    EditMessageText answer = new EditMessageText(chatId, messageId, workSchedule);
                    logger.warn("IMPORTANT" + workSchedule);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                break;
            case "address":
                try {
                    String address = "Улица чертовых тестов";
                    EditMessageText answer = new EditMessageText(chatId, messageId, address);
                    logger.warn("IMPORTANT" + address);
                    telegramBot.execute(answer);
                } catch (Exception e) {
                    logger.warn("Ошибка в кейсе address: " + e);
                }
                break;
            case "way":
                try {
                    String way = "Путь через боль";
                    EditMessageText answer = new EditMessageText(chatId, messageId, way);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                break;
            case "safety":
                try {
                    String safety = "тестовый ответ";
                    EditMessageText answer = new EditMessageText(chatId, messageId, safety);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                break;
            case "volunteer":
                telegramBot.execute(new EditMessageText(chatId, messageId, "Спасибо за обращение, волонтер приюта свяжется " +
                        "с Вами"));
                break;
            case "contact":
                telegramBot.execute(new EditMessageText(chatId, messageId, COMMAND_SAFE_CONTACT_DETAILS_FOR_COMMUNICATION.getText()));
                break;
            case "rules":
                try {
                    String information = RULES_FOR_DATING_CAT.getMessage();
                    EditMessageText answer = new EditMessageText(chatId, messageId,  RULES_FOR_DATING_CAT.getMessage());
                    logger.warn("IMPORTANT" + information);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                logger.info("Кнопка 1 работает");
                break;
            case "docs":
                try {
                    String information = LIST_DOCUMENTS_FOR_CAT.getMessage();
                    EditMessageText answer = new EditMessageText(chatId, messageId, information);
                    logger.warn("IMPORTANT" + information);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                logger.info("Кнопка 2 работает");
                break;
            case "transportation":
                try {
                    String information = RECOMMENDATIONS_FOR_TRANSPORTATION.getMessage();
                    EditMessageText answer = new EditMessageText(chatId, messageId, information);
                    logger.warn("IMPORTANT" + information);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                logger.info("Кнопка 3 работает");
                break;
            case "arrangementKitty":
                try {
                    String information = RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_KITTEN.getMessage();
                    EditMessageText answer = new EditMessageText(chatId, messageId, information);
                    logger.warn("IMPORTANT" + information);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                logger.info("Кнопка 4 работает");
                break;
            case "arrangementCat":
                try {
                    String information = RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_ADULT_CAT.getMessage();
                    EditMessageText answer = new EditMessageText(chatId, messageId, information);
                    logger.warn("IMPORTANT" + information);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                logger.info("Кнопка 5 работает");
                break;
            case "arrangementCatDisabled":
                try {
                    String information = RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_DISABLED_CAT.getMessage();
                    EditMessageText answer = new EditMessageText(chatId, messageId, information);
                    logger.warn("IMPORTANT" + information);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                logger.info("Кнопка 6 работает");
                break;
            case "reject":
                try {
                    String information = REASONS_FOR_REFUSAL_CAT.getMessage();
                    EditMessageText answer = new EditMessageText(chatId, messageId, information);
                    logger.warn("IMPORTANT" + information);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                logger.info("Кнопка 9 работает");
                break;
        }
    }

}
