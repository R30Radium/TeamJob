package telegram.teamjob.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telegram.teamjob.constants.BotButtonEnum;
import telegram.teamjob.constants.BotMessageEnum;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private boolean flag;
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final UserService userService;
    private final RecordService recordService;
    private final PetPhotoService petPhotoService;

    @Autowired
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(UserService userService, RecordService recordService, PetPhotoService petPhotoService) {
        this.userService = userService;
        this.recordService = recordService;
        this.petPhotoService = petPhotoService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            try {
                messageHandler(update);
            } catch (Exception e) {
                throw new RuntimeException("ERROR");
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void messageHandler(Update update) {
        if (update.callbackQuery() != null) {
            logger.info("CallBackQuery processing");
            checkButtonAnswer(update);
        } else if (update.message().photo() != null) {
            logger.info("Photo Upload processing");
            long recordId = recordService.findRecordId(update);
            PhotoSize[] photos = update.message().photo();
            try {
                petPhotoService.uploadPhoto(recordId, photos);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            logger.info("Processing update: {}", update);
            checkAnswer(update);
        }
    }

    private SendMessage sendMessage(long chatId, String textToSend) {
        return new SendMessage(chatId, textToSend);
    }

    /**
     * При старте бота будет выведена информация и предложено меню бота
     */
    private void sendGreetingMessage(Update update) {
        String greetingMessage = BotMessageEnum.START_MESSAGE.getMessage();
        telegramBot.execute(sendMessage(update.message().chat().id(), greetingMessage)
                .replyMarkup(ourMenuButtons()));
        logger.info("Message Sent" + " Method - sendGreeting");
    }

    private void checkAnswer(Update update) {
        Long chatId = update.message().chat().id();
        String inputText = update.message().text();
        if (inputText != null) {
            if (inputText.equals("/start")) {
                sendGreetingMessage(update);
            } else if (inputText.equals("/createuser")) {
                telegramBot.execute(sendMessage(chatId, BotMessageEnum.CREATE_USER_INFO.getMessage()));
            } else if (inputText.matches("([A-zА-я]+)(\\s)([0-9]+)(\\s)([A-zА-я]+)")) {
                logger.info("Processing checkAnswer:");
                userService.createUser(update);
            } else if (inputText.equals("/send-record")) {
                String messageForRecords = BotMessageEnum.DAILY_RECORD_INFO.getMessage();
                telegramBot.execute(sendMessage(chatId, messageForRecords));
            } else if (flag) {
                //При нажатии на кнопку 3 (Отправить отчет)
                logger.info("Processing creating record");
                // Нужен if На случай некорректного сообщения
                if (userService.getUser(chatId) != null) {
                    recordService.createRecord(update);
                } else {
                    telegramBot.execute(sendMessage(chatId, BotMessageEnum.USER_NOT_FOUND_MESSAGE.getMessage()));
                }
                flag = false;
            } else {
                telegramBot.execute(sendMessage(chatId, BotMessageEnum.ASK_HELP.getMessage()));
            }
        } else {
            telegramBot.execute(sendMessage(chatId, BotMessageEnum.ASK_HELP.getMessage()));
        }
    }

    /**
     * Метод добавляет под сообщение кнопки с меню.
     * Клиент может выбрать интересующий его пункт и получить информацию
     */
    private InlineKeyboardMarkup ourMenuButtons() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        var button1Info = new InlineKeyboardButton(BotButtonEnum.BUTTON_INFO.getMessage());
        var button2Instruction = new InlineKeyboardButton(BotButtonEnum.BUTTON_INSTRUCTION.getMessage());
        var button3Record = new InlineKeyboardButton(BotButtonEnum.BUTTON_RECORD.getMessage());
        var button4Help = new InlineKeyboardButton(BotButtonEnum.BUTTON_HELP.getMessage());

        button1Info.callbackData(BotButtonEnum.BUTTON_INFO.getMessage());
        button2Instruction.callbackData(BotButtonEnum.BUTTON_INSTRUCTION.getMessage());
        button3Record.callbackData(BotButtonEnum.BUTTON_RECORD.getMessage());
        button4Help.callbackData(BotButtonEnum.BUTTON_HELP.getMessage());

        /* Данные кнопки будут располагаться друг под другом. Можно использоваться в линию
          для этого необходимые кнопки можно перечислить в параметрах markup.addRow(b1, b2, b3, b4);
          Тогда информация будет сжата по размеру кнопки
         */
        markup.addRow(button1Info);
        markup.addRow(button2Instruction);
        markup.addRow(button3Record);
        markup.addRow(button4Help);
        return markup;
    }

    /**
     * Проверяем что выбрал пользователь, чтобы заменить текст сообщения на вывод информации
     */
    private void checkButtonAnswer(Update update) {

        String callBackData = update.callbackQuery().data();
        long messageId = update.callbackQuery().message().messageId();
        long chatId = update.callbackQuery().message().chat().id();
        switch (callBackData) {
            case "Узнать информацию о приюте":
                // Заглушка. Необходимо поменять на вызов метода получения информации (Этап1 п2)
                String newMessage = "Кнопка 1 работает";
                EditMessageText messageText = new EditMessageText(chatId, (int) messageId, newMessage);
                telegramBot.execute(messageText);
                break;
            case "Как взять собаку из приюта":
                // Необходимо заменить на вывод инструкции
                String newMessage2 = "Кнопка 2 работает";
                EditMessageText messageText2 = new EditMessageText(chatId, (int) messageId, newMessage2);
                telegramBot.execute(messageText2);
                break;
            case "Прислать отчет о питомце":
                //Отчет по питомцу
                String messageForRecords = BotMessageEnum.DAILY_RECORD_INFO.getMessage();
                EditMessageText messageText3 = new EditMessageText(chatId, (int) messageId, messageForRecords);
                telegramBot.execute(messageText3);
                flag = true;
                break;
            case "Позвать волонтера":
                //Обращение к волонтеру
                String newMessage4 = BotMessageEnum.ASK_HELP.getMessage();
                EditMessageText messageText4 = new EditMessageText(chatId, (int) messageId, newMessage4);
                telegramBot.execute(messageText4);
                break;
        }
    }
}
