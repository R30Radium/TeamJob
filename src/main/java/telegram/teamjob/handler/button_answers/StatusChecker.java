package telegram.teamjob.handler.button_answers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.EditMessageText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.entity.Client;
import telegram.teamjob.implementation.ClientServiceImpl;
import telegram.teamjob.implementation.ShelterServiceImpl;
import telegram.teamjob.implementation.TelegramBotUpdatesListener;
import telegram.teamjob.implementation.VolunteerServiceImpl;

import static telegram.teamjob.constants.BotMessageEnum.*;

@Service
public class StatusChecker {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final ShelterServiceImpl shelterService;
    private final VolunteerServiceImpl volunteerService;
    private final ClientServiceImpl clientService;

    public StatusChecker(TelegramBot telegramBot, ShelterServiceImpl shelterService, VolunteerServiceImpl volunteerService, ClientServiceImpl clientService) {
        this.telegramBot = telegramBot;
        this.shelterService = shelterService;
        this.volunteerService = volunteerService;
        this.clientService = clientService;
    }

    private void checkButtonAnswerForStatus(Update update) {
        logger.info("Проверка ответа Пользователь/Волонтер");
        String callBackData = update.callbackQuery().data();
        logger.info(callBackData);
        int messageId = update.callbackQuery().message().messageId();
        long chatId = update.callbackQuery().message().chat().id();
        switch (callBackData) {
            case "Волонтер":
                //вызов  ответа для волонтера
                telegramBot.execute(new EditMessageText(chatId,messageId, START_MESSAGE_VOLUNTEER.getMessage()).
                        replyMarkup(volunteerService.menuForVolunteer()));
                logger.info("Отправил меню для волонтера");
                break;
            case "Пользователь":
                telegramBot.execute(new EditMessageText(chatId,messageId,
                        START_SHELTER_OPTION.getMessage()).replyMarkup(volunteerService.shelterStatusMenuButtons()));
                logger.info("Отправил пользователю меню для выбора приюта");
                break;
        }
    }

    private void checkButtonShelterStatus(Update update) {
        logger.info("Проверка выбора приюта");
        String callBackData = update.callbackQuery().data();
        logger.info(callBackData);
        int messageId = update.callbackQuery().message().messageId();
        long chatId = update.callbackQuery().message().chat().id();
        Client client = new Client();
        client.setChatId(chatId);
        client.setStatus(0);
        switch (callBackData) {
            case "Приют для собак":
                logger.info("Выбран приют для собак");
                client.setStatus(1);
                setClientStatus(chatId, client);
                telegramBot.execute(new EditMessageText(chatId, messageId,
                        START_MESSAGE_USER_DOG.getMessage()).replyMarkup(shelterService.ourMenuButtons()));
                logger.info("Отправил пользователю меню для собак");
                break;
            case "Приют для кошек":
                logger.info("Выбран приют для кошек");
                client.setStatus(2);
                setClientStatus(chatId, client);
                telegramBot.execute(new EditMessageText(chatId, messageId,
                        START_MESSAGE_USER_CAT.getMessage()).replyMarkup(shelterService.ourMenuCatButtons()));
                logger.info("Отправил пользователю меню для кошек");
                break;
        }
    }

    public void checkStartAnswers(Update update) {
        logger.info("Методы проверки стартового меню");
        checkButtonAnswerForStatus(update);
//        checkButtonShelterStatus(update);
    }

    public void shelterStatus(Update update) {
        checkButtonShelterStatus(update);
    }

    private void setClientStatus(Long chatId, Client client) {
        if (clientService.findClient(chatId) == null) {
            clientService.saveClient(client);
        } else {
            clientService.updateClient(client);
        }
    }
}


