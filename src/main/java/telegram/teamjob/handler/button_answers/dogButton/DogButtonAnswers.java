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
import static telegram.teamjob.constants.Cat.TextForResponseFromMenuButtonsDog.RULES_FOR_DATING_DOG;

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
                logger.info("case");
                try {
                    logger.info("try case 1");
                    String information = "Информация о приюте";
                    SendMessage answer = new SendMessage(chatId, information);
                    logger.warn("IMPORTANT" + information);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                break;
            case "workTime":
                try {
//                        String workSchedule = shelter.getWorkScheduleShelter();
                    String workSchedule = "24/7 без перерывов";
                    SendMessage answer = new SendMessage(chatId, workSchedule);
                    logger.warn("IMPORTANT" + workSchedule);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                break;
            case "address":
                try {
//                    String address = shelter.getAddressShelter();
                    String address = "Улица чертовых тестов";
                    SendMessage answer = new SendMessage(chatId, address);
                    logger.warn("IMPORTANT" + address);
                    telegramBot.execute(answer);
                } catch (Exception e) {
                    logger.warn("Ошибка в кейсе address: " + e);
                }
                break;
            case "way":
                try {
//                        String way = shelter.getDrivingDirectionsShelter();
                    String way = "Путь через боль";
                    SendMessage answer = new SendMessage(chatId, way);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                break;
            case "safety":
                try {
//                        String safety = shelter.getSafetyAtShelter();
                    String safety = "тестовый ответ";
                    SendMessage answer = new SendMessage(chatId, safety);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                break;
            case "volunteer":
                telegramBot.execute(new SendMessage(chatId, "Спасибо за обращение, волонтер приюта свяжется " +
                        "с Вами"));
                break;
            case "contact":
                telegramBot.execute(new SendMessage(chatId, COMMAND_SAFE_CONTACT_DETAILS_FOR_COMMUNICATION.getText()));
                break;
            case "rules":
                try {
//                        String information = infoOwner.getRules();
                    String information = "Забери 2 собаки";
                    SendMessage answer = new SendMessage(chatId,  RULES_FOR_DATING_DOG.getMessage());
                    logger.warn("IMPORTANT" + information);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                logger.info("Кнопка 1 работает");
                break;
            case "docs":
                try {
                    String information = "Документы";
                    SendMessage answer = new SendMessage(chatId, information);
                    logger.warn("IMPORTANT" + information);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                logger.info("Кнопка 2 работает");
                break;
            case "transportation":
                try {
//                        String information = infoOwner.getTranspartation();
                    String information = "Перевозка";
                    SendMessage answer = new SendMessage(chatId, information);
                    logger.warn("IMPORTANT" + information);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                logger.info("Кнопка 3 работает");
                break;
            case "arrangementPuppy":
                try {
                    String information = "Щеночки";
                    SendMessage answer = new SendMessage(chatId, information);
                    logger.warn("IMPORTANT" + information);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                logger.info("Кнопка 4 работает");
                break;
            case "arrangementDog":
                try {
                    String information = "Собачки";
                    SendMessage answer = new SendMessage(chatId, information);
                    logger.warn("IMPORTANT" + information);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                logger.info("Кнопка 5 работает");
                break;
            case "arrangementDogInvalid":
                try {
                    String information = "Собачки 2";
                    SendMessage answer = new SendMessage(chatId, information);
                    logger.warn("IMPORTANT" + information);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                logger.info("Кнопка 6 работает");
                break;
            case "cynologist":
                try {
                    String information = "Тест";
                    SendMessage  answer = new SendMessage(chatId, information);
                    logger.warn("IMPORTANT" + information);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                logger.info("Кнопка 7 работает");
                break;
            case "goodCynologists":
                try {
                    String information = "Тест 2";
                    SendMessage answer = new SendMessage(chatId, information);
                    logger.warn("IMPORTANT" + information);
                    telegramBot.execute(answer);
                } catch (NullPointerException e) {
                    logger.warn("Инфopмации в базе данных нет");
                }
                logger.info("Кнопка 8 работает");
                break;
            case "reject":
                try {
                    String information = "Тест3";
                    SendMessage answer = new SendMessage(chatId, information);
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
