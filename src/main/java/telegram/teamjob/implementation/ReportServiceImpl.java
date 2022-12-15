package telegram.teamjob.implementation;


import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.Service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

    Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Override
    public InlineKeyboardMarkup makeButtonsForMenuStageThreeForReport() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        String photo = " Фото питомца ";
        String record = " Текстовый отчет  ";

        var buttonPhoto = new InlineKeyboardButton(photo);
        var buttonRecord = new InlineKeyboardButton(record);

        buttonPhoto.callbackData("photo");
        buttonRecord.callbackData("record");

        markup.addRow(buttonPhoto);
        markup.addRow(buttonRecord);

        logger.info("Отправил меню третьего этапа");
        return markup;
    }
}

