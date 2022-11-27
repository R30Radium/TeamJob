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

    // При старте бота будет выведена информация и предложено меню бота
    private void sendGreetingMessage(Update update) {
        String greetingMessage = "Здравствуй @" + update.message().chat().username() + "!\n" +
                "Рады приветствовать Вас в нашем Приюте для Животных\n" +
                "Данный бот поможет вам, если вы задумываетесь о том, чтобы забрать собаку или кошку домой\n" +
                "Выберите пункт меню, который вас интересует";
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
                telegramBot.execute(sendMessage(chatId, "Введите данные в формате: Имя мобильный имя петомца (при наличии)"));
            } else if (inputText.matches("([A-zА-я]+)(\\s)([0-9]+)(\\s)([A-zА-я]+)")) {
                logger.info("Processing checkAnswer:");
                userService.createUser(update);
            } else if (inputText.equals("/send-record")) {
                String messageForRecords = "В ежедневный отчет входит следующая информация: \n" +
                        "\n" +
                        "- Фото животного.\n" +
                        "- Рацион животного.\n" +
                        "- Общее самочувствие и привыкание к новому месту.\n" +
                        "- Изменение в поведении: отказ от старых привычек, приобретение новых.\n" +
                        "\n" +
                        "Отчет нужно присылать каждый день, ограничений в сутках по времени сдачи отчета нет.";
                telegramBot.execute(sendMessage(chatId, messageForRecords));
            } else if (flag) {
                logger.info("Processing creating record");
                // Нужен if На случай некорректного сообщения
                if (userService.getUser(chatId) != null) {
                    recordService.createRecord(update);
                } else {
                    telegramBot.execute(sendMessage(chatId, "Вас нет в базе\n" +
                            "Введите данные в формате: Имя мобильный имя петомца (при наличии)\n" +
                            "И пришлите отчет ещё раз"));
                }
                flag = false;
            } else {
                telegramBot.execute(sendMessage(chatId, "Вы можете обратиться к волонтеру @LnBgrn"));
            }
        } else {
            telegramBot.execute(sendMessage(chatId, "Вы можете обратиться к волонтеру @LnBgrn"));
        }
    }

    /*
        Метод добавляет под сообщение кнопки с меню.
        Клиент может выбрать интересующий его пункт и получить информацию
     */
    private InlineKeyboardMarkup ourMenuButtons() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        String info = "Узнать информацию о приюте";
        String instruction = "Как взять собаку из приюта";
        String record = "Прислать отчет о питомце";
        String help = "Позвать волонтера";

        var button1Info = new InlineKeyboardButton(info);
        var button2Instruction = new InlineKeyboardButton(instruction);
        var button3Record = new InlineKeyboardButton(record);
        var button4Help = new InlineKeyboardButton(help);

        button1Info.callbackData("TEXT1_BUTTON");
        button2Instruction.callbackData("TEXT2_BUTTON");
        button3Record.callbackData("TEXT3_BUTTON");
        button4Help.callbackData("TEXT4_BUTTON");

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

    /*
    Проверяем что выбрал пользователь, чтобы заменить текст сообщения на вывод информации
     */
    private void checkButtonAnswer(Update update) {
        String callBackData = update.callbackQuery().data();
        long messageId = update.callbackQuery().message().messageId();
        long chatId = update.callbackQuery().message().chat().id();
        switch (callBackData) {
            case "TEXT1_BUTTON":
                // Заглушка. Необходимо поменять на вызов метода получения информации (Этап1 п2)
                String newMessage = "Кнопка 1 работает";
                EditMessageText messageText = new EditMessageText(chatId, (int) messageId, newMessage);
                telegramBot.execute(messageText);
                break;
            case "TEXT2_BUTTON":
                // Необходимо заменить на вывод инструкции
                String newMessage2 = "Кнопка 2 работает";
                EditMessageText messageText2 = new EditMessageText(chatId, (int) messageId, newMessage2);
                telegramBot.execute(messageText2);
                break;
            case "TEXT3_BUTTON":
                //Отчет по питомцу
                String messageForRecords = "*В ежедневный отчет входит следующая информация:* \n" +
                        "\n" +
                        "- *Фото животного.*\n" +
                        "- *Рацион животного.*\n" +
                        "- *Общее самочувствие и привыкание к новому месту.*\n" +
                        "- *Изменение в поведении: отказ от старых привычек, приобретение новых.*\n" +
                        "\n" +
                        "*Отчет нужно присылать каждый день, ограничений в сутках по времени сдачи отчета нет.*";
                EditMessageText messageText3 = new EditMessageText(chatId, (int) messageId, messageForRecords);
                telegramBot.execute(messageText3);
                 flag = true;
                break;
            case "TEXT4_BUTTON":
                //Обращение к волонтеру
                String newMessage4 = "Вы можете обратиться к волонтеру @LnBgrn";
                EditMessageText messageText4 = new EditMessageText(chatId, (int) messageId, newMessage4);
                telegramBot.execute(messageText4);
                break;
        }
    }
}
