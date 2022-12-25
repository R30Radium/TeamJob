package telegram.teamjob.handler.button_answers.clientMessageHandler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.constants.BotMessageEnum;
import telegram.teamjob.handler.DataCheck;
import telegram.teamjob.handler.button_answers.StatusChecker;
import telegram.teamjob.implementation.*;
import telegram.teamjob.implementation.Cat.CatPhotoServiceImpl;
import telegram.teamjob.implementation.Cat.RecordCatServiceImpl;
import telegram.teamjob.implementation.Cat.UserCatServiceImpl;
import telegram.teamjob.repositories.Cat.RecordCatRepository;
import telegram.teamjob.repositories.RecordRepository;
import telegram.teamjob.repositories.UserRepository;

import java.io.IOException;

import static telegram.teamjob.constants.BotButtonEnum.BUTTON_CHECK_CONTACT;
import static telegram.teamjob.constants.BotMessageEnum.*;

@Service
public class MessageHendlerClient {
    private final TelegramBot telegramBot;
    private final ClientServiceImpl clientService;
    private final ContactServiceImpl contactService;
    private final RecordServiceImpl recordService;
    private final PetPhotoServiceImpl petPhotoService;
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final RecordCatServiceImpl recordCatService;
    private final CatPhotoServiceImpl catPhotoService;
    private final RecordCatRepository recordCatRepository;
    private final UserCatServiceImpl userCatService;
    private final DataCheck dataCheck;
    private final StatusChecker statusChecker;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    public MessageHendlerClient(TelegramBot telegramBot, ClientServiceImpl clientService, ContactServiceImpl contactService, RecordServiceImpl recordService, PetPhotoServiceImpl petPhotoService, RecordRepository recordRepository, UserRepository userRepository, RecordCatServiceImpl recordCatService, CatPhotoServiceImpl catPhotoService, RecordCatRepository recordCatRepository, UserCatServiceImpl userCatService, DataCheck dataCheck, StatusChecker statusChecker) {
        this.telegramBot = telegramBot;
        this.clientService = clientService;
        this.contactService = contactService;
        this.recordService = recordService;
        this.petPhotoService = petPhotoService;
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
        this.recordCatService = recordCatService;
        this.catPhotoService = catPhotoService;
        this.recordCatRepository = recordCatRepository;
        this.userCatService = userCatService;
        this.dataCheck = dataCheck;
        this.statusChecker = statusChecker;
    }


    public void checkMessageFromCatClient(Update update) {
        if (update.callbackQuery() != null) {
            logger.info("CallBackQuery processing for CatClient");
            dataCheck.checkCatButtonAnswer(update);
            if (update.callbackQuery().data().equals("photo") ||//команды для меню третьего этапа(отчет)
                    update.callbackQuery().data().equals("record")) {
                sendResponseForThirdMenu(update);
            } else if (update.callbackQuery().data().equals(BUTTON_CHECK_CONTACT.getMessage())) {
                contactService.getAllContactsForVolunteer(update);
            }
        } else if (update.message().photo() != null) {
            logger.info("Photo Upload processing");
            Long recordId = recordCatService.findRecordCatId(update);
            PhotoSize[] photos = update.message().photo();
            if (recordId != null) {
                try {
                    catPhotoService.uploadPhoto(recordId, photos);
                    telegramBot.execute(sendMessage(update.message().chat().id(), "Ваше фото сохранено"));
                } catch (IOException e) {
                    logger.info("Error" + e);
                    telegramBot.execute(sendMessage(update.message().chat().id(), BotMessageEnum.ASK_HELP.getMessage()));
                }
            } else if (recordCatRepository.findAll().isEmpty()) {
                logger.info("Таблица с отчетами пуста");
                if (userCatService.findUsers(update.message().chat().id()).isEmpty()) {
                    logger.info("Метод добавления фото: Нет пользователя");
                    telegramBot.execute(sendMessage(update.message().chat().id(), "Сперва необходимо отправить отчёт\n" +
                            USER_NOT_FOUND_MESSAGE));
                } else {
                    telegramBot.execute(sendMessage(update.message().chat().id(), "Сперва необходимо отправить отчёт\n" +
                            BotMessageEnum.DAILY_RECORD_INFO));
                }
            } else {
                logger.info("Фото не добавлено");
            }
        }

    }

    public void checkMessageFromDogClient(Update update) {
        if (update.callbackQuery() != null) {
            logger.info("CallBackQuery processing for DogClient");
            dataCheck.checkDogButtonAnswer(update);
            if (update.callbackQuery().data().equals("photo") ||//команды для меню третьего этапа(отчет)
                    update.callbackQuery().data().equals("record")) {
                sendResponseForThirdMenu(update);
            } else if (update.callbackQuery().data().equals(BUTTON_CHECK_CONTACT.getMessage())) {
                contactService.getAllContactsForVolunteer(update);
            }
        } else if (update.message().photo() != null) {
            logger.info("Photo Upload processing");
            Long recordId = recordService.findRecordId(update);
            PhotoSize[] photos = update.message().photo();
            if (recordId != null) {
                try {
                    petPhotoService.uploadPhoto(recordId, photos);
                    telegramBot.execute(sendMessage(update.message().chat().id(), "Ваше фото сохранено"));
                } catch (IOException e) {
                    logger.info("Error" + e);
                    telegramBot.execute(sendMessage(update.message().chat().id(), BotMessageEnum.ASK_HELP.getMessage()));
                }
            } else if (recordRepository.findAll().isEmpty()) {
                logger.info("Таблица с отчетами пуста");
                if (userRepository.findAllUsersByChatId(update.message().chat().id()).isEmpty()) {
                    logger.info("Метод добавления фото: Нет пользователя");
                    telegramBot.execute(sendMessage(update.message().chat().id(), "Сперва необходимо отправить отчёт\n" +
                            USER_NOT_FOUND_MESSAGE));
                } else {
                    telegramBot.execute(sendMessage(update.message().chat().id(), "Сперва необходимо отправить отчёт\n" +
                            BotMessageEnum.DAILY_RECORD_INFO));
                }
            } else {
                logger.info("Фото не добавлено");
            }
        }
    }
    private SendMessage sendMessage(long chatId, String textToSend) {
        return new SendMessage(chatId, textToSend);
    }
    private void sendResponseForThirdMenu(Update update) {
        logger.info(" вызван метод sendResponseForThirdMenu");
        String answerMenu = update.callbackQuery().data();
        long chatId = update.callbackQuery().message().chat().id();
        //  int messageId = update.callbackQuery().message().messageId();
        switch (answerMenu) {
            case "photo":
                //  telegramBot.execute(new EditMessageText(chatId, messageId, PHOTO.getMessage()));
                telegramBot.execute(new SendMessage(chatId, PHOTO.getMessage()));
                logger.warn("IMPORTANT" + PHOTO.getMessage());
                break;
            case "record":
                //   EditMessageText answer2 = new EditMessageText(chatId, messageId, RECORD.getMessage());
                telegramBot.execute(new SendMessage(chatId, RECORD.getMessage()));
                logger.warn("send info for client about report");
                break;
        }
    }

    public void startMessage(Update update) {
        logger.info("Проверка стартового меню");
        statusChecker.checkStartAnswers(update);
        statusChecker.shelterStatus(update);

    }

    public void checkShelterStatus(Update update) {
        statusChecker.shelterStatus(update);
    }
}
