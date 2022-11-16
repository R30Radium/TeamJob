package telegram.teamjob.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
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
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            if (update.callbackQuery() == null) {
                logger.info("Processing update: {}", update);
                checkAnswer(update);
            }
            else if (update.callbackQuery() != null) {
                logger.info("CallBackQuery processing");
                checkButtonAnswer(update);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
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
        switch (update.message().text()) {
            case "/start":
                sendGreetingMessage(update);
                logger.info("checkAnswer: case /start");
                break;
            default:
                telegramBot.execute(sendMessage(update.message().chat().id(),
                        "Вы можете обратиться к волонтеру @LnBgrn"));
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
                String newMessage3 = "Кнопка 3 работает";
                EditMessageText messageText3 = new EditMessageText(chatId, (int) messageId, newMessage3);
                telegramBot.execute(messageText3);
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
