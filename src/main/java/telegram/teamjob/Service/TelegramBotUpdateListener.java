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
public class TelegramBotUpdateListener implements UpdatesListener {
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdateListener.class);


    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /* делать через кучу вложенного кода в метод process оказалось не вариантом,
    поэтому попробуем вот так */

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

    private void sendGreet(Update update) {
        String greetingMessage = "Здравствуй " + update.message().chat().username() + "!\n" +

                "Рады приветствовать в приюте для животных\n" +

                "Данный бот поможет вам, если вы задумываетесь о том, " +

                "чтобы забрать питомца себе домой\n" +

                "Выберите функцию в меню, которая вас интересует";

        telegramBot.execute(sendMessage(update.message().chat().id(), greetingMessage)
                .replyMarkup(menuButtons()));

        logger.info("Message Sent" + " Method - sendGreet");

    }

    /*Возможно лучше отказаться от использования switch?*/
    private void checkAnswer(Update update) {
        switch (update.message().text()) {
            case "/start":
                sendGreet(update);
                logger.info("checkAnswer: case /start");
                break;
            default:
                telegramBot.execute(sendMessage(update.message().chat().id(),
                        "Вы можете обратиться к волонтеру @RRRNikita"));
        }
    }

    /*
        Метод добавляет под сообщение кнопки с меню.
        Клиент может выбрать интересующий его пункт и получить информацию
     */
    private InlineKeyboardMarkup menuButtons() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        String rules = " Правила знакомства с животным ";
        String docs = " Список необходимых документов ";
        String transportation = " Рекомендация по транспортировке ";
        String arrangementPuppy = " Рекомендации по устройству дома для щенка ";
        String arrangementDog = " Рекомендации по устройству дома для взрослой собаки ";
        String arrangementDogInvalid = " Рекомендации по устройству дома для собаки с " +
                " ограниченными возможностями ";
        String cynologist= " Рекомендации по общению с собакой от кинологов";
        String reject = " Спиоск причин отказа ";
        String contact = " Контактные данные ";
        String help = " Позвать волонтера ";


        var button1Rules = new InlineKeyboardButton(rules);
        var button2Docs = new InlineKeyboardButton(docs);
        var button3Transportation = new InlineKeyboardButton(transportation);
        var button4ArrangementPuppy = new InlineKeyboardButton(arrangementPuppy);
        var button5ArrangementDog = new InlineKeyboardButton(arrangementDog);
        var button6ArrangementDogInvalid = new InlineKeyboardButton(arrangementDogInvalid);
        var button7Cynologist = new InlineKeyboardButton(cynologist);
        var button8Reject = new InlineKeyboardButton(reject);
        var button9Contact = new InlineKeyboardButton(contact);
        var button10Help = new InlineKeyboardButton(help);

        button1Rules.callbackData("TEXT1_BUTTON");
        button2Docs.callbackData("TEXT2_BUTTON");
        button3Transportation.callbackData("TEXT3_BUTTON");
        button4ArrangementPuppy.callbackData("TEXT4_BUTTON");
        button5ArrangementDog.callbackData("TEXT5_BUTTON");
        button6ArrangementDogInvalid.callbackData("TEXT6_BUTTON");
        button7Cynologist.callbackData("TEXT7_BUTTON");
        button8Reject.callbackData("TEXT8_BUTTON");
        button9Contact.callbackData("TEXT9_BUTTON");
        button10Help.callbackData("TEXT10_BUTTON");


        /* Данные кнопки будут располагаться друг под другом. Можно использоваться в линию
           для этого необходимые кнопки можно перечислить
            в параметрах markup.addRow(b1, b2, b3, b4);
           Тогда информация будет сжата по размеру кнопки
        */
        markup.addRow(button1Rules);
        markup.addRow(button2Docs);
        markup.addRow(button3Transportation);
        markup.addRow(button4ArrangementPuppy);
        markup.addRow(button5ArrangementDog);
        markup.addRow(button6ArrangementDogInvalid);
        markup.addRow(button7Cynologist);
        markup.addRow(button8Reject);
        markup.addRow(button9Contact);
        markup.addRow(button10Help);
        return markup;
    }

    /*
    Проверяем что выбрал пользователь, чтобы заменить текст сообщения на вывод информации
     */
    private void checkButtonAnswer(Update update) {
        String callBackData = update.callbackQuery().data();
        /* messageID наверное можно было бы int */
        long messageId = update.callbackQuery().message().messageId();
        long chatId = update.callbackQuery().message().chat().id();
        switch (callBackData) {
            case "TEXT1_BUTTON":
                // Правила
                String newMessage = "Кнопка 1 работает";
                EditMessageText messageText = new EditMessageText(chatId,
                        (int) messageId, newMessage);
                telegramBot.execute(messageText);
                break;
            case "TEXT2_BUTTON":
                // Необходимо заменить на вывод инструкции
                String newMessage2 = "Кнопка 2 работает";
                EditMessageText messageText2 = new EditMessageText(chatId,
                        (int) messageId, newMessage2);
                telegramBot.execute(messageText2);
                break;
            case "TEXT3_BUTTON":
                //Отчет по питомцу
                String newMessage3 = "Кнопка 3 работает";
                EditMessageText messageText3 = new EditMessageText(chatId,
                        (int) messageId, newMessage3);
                telegramBot.execute(messageText3);
                break;
            case "TEXT4_BUTTON":
                //Обращение к волонтеру
                String newMessage4 = "Вы можете обратиться к волонтеру @RRRNikita";
                EditMessageText messageText4 = new EditMessageText(chatId,
                            (int) messageId, newMessage4);
                telegramBot.execute(messageText4);
                break;
            case "TEXT5_BUTTON":
                // Рекомендации щенку
                String newMessage5 = "Кнопка 5 работает";
                EditMessageText messageText5 = new EditMessageText(chatId,
                        (int) messageId, newMessage5);
                telegramBot.execute(messageText5);
                break;
            case "TEXT6_BUTTON":
                // рекомендации взрослой собаке
                String newMessage6 = "Кнопка 6 работает";
                EditMessageText messageText6 = new EditMessageText(chatId,
                        (int) messageId, newMessage6);
                telegramBot.execute(messageText6);
                break;
            case "TEXT7_BUTTON":
                // кинолог
                String newMessage7 = "Кнопка 7 работает";
                EditMessageText messageText7 = new EditMessageText(chatId,
                        (int) messageId, newMessage7);
                telegramBot.execute(messageText7);
                break;
            case "TEXT8_BUTTON":
                // контакты
                String newMessage8 = "Кнопка 8 работает";
                EditMessageText messageText8 = new EditMessageText(chatId,
                        (int) messageId, newMessage8);
                telegramBot.execute(messageText8);
                break;

            case "TEXT9_BUTTON":
                // возможнные причины для отказа
                String newMessage9 = "Кнопка 9 работает";
                EditMessageText messageText9 = new EditMessageText(chatId,
                        (int) messageId, newMessage9);
                telegramBot.execute(messageText9);
                break;

            case "TEXT10_BUTTON":
                //Обращение к волонтеру
                String newMessage10 = "Кнопка 10 работает";
                EditMessageText messageText10 = new EditMessageText(chatId,
                        (int) messageId, newMessage10);
                telegramBot.execute(messageText10);
                break;

        }
    }
}