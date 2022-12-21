package telegram.teamjob.implementation.Cat;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegram.teamjob.Service.Cat.RecordCatService;
import telegram.teamjob.entity.Cat.RecordCat;
import telegram.teamjob.repositories.Cat.RecordCatRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;

import static telegram.teamjob.constants.BotMessageEnum.*;
import static telegram.teamjob.constants.BotMessageEnum.SAVE_INFORMATION;
@Service
public class RecordCatServiceImpl implements RecordCatService {
    private Logger logger = LoggerFactory.getLogger(RecordCatServiceImpl.class);
    private final RecordCatRepository recordCatRepository;
    private TelegramBot telegramBot;


    public RecordCatServiceImpl(RecordCatRepository recordCatRepository,
                             TelegramBot telegramBot) {
        this.recordCatRepository = recordCatRepository;
        this.telegramBot = telegramBot;

    }

    @Override
    public Long findRecordCatId(Update update) {
        Long chatId = update.message().chat().id();
        LinkedList<RecordCat> recordsList = recordCatRepository.findAllRecordCatByChatId(chatId);
        RecordCat recordCat = recordsList.peekLast();
        if (recordCat != null) {
            return recordCat.getRecordCatId();
        } else if (recordCatRepository.findAll().isEmpty()) {
            telegramBot.execute(new SendMessage(chatId, "Для начала отчёт"));
        } else {
            return recordCatRepository.findRecordCatByChatId(chatId).getRecordCatId();
        }
        return null;
    }
    /**
     * метод сохраняет в базе данных часть отчета отвечающую за информацию  <br>
     * об измеении в поведении питомца <br>
     * see ChangeInBehavior
     * @param update
     */
    @Override
    public void saveRecordCat (Update update) {
        logger.info("Процесс сохранения отчета");
        LocalDateTime localDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        String recordCat = update.message().text();
        long chatId = update.message().chat().id();
        int dietIndex = recordCat.indexOf("Диета:");
        int adaptationIndex = recordCat.indexOf("Адаптация:");
        int behaviorIndex = recordCat.indexOf("Изменение в поведении:");

        String dietResult = recordCat.substring(dietIndex, adaptationIndex);
        String adaptationResult = recordCat.substring(adaptationIndex, behaviorIndex);
        String behaviorResult = recordCat.substring(behaviorIndex, recordCat.length());


        if (dietResult.length() < 8){
            telegramBot.execute(new SendMessage(chatId,RECORD_DIETA.getMessage()));
        }
        else if(adaptationResult.length() < 8){
            telegramBot.execute(new SendMessage(chatId, RECORD_ADAPTATION.getMessage()));
        }
        else if(behaviorResult.length()< 8) {
            telegramBot.execute(new SendMessage(chatId,RECORD_BEHAVIOR.getMessage()));
        } else {
            RecordCat recordForBase = new RecordCat();
            recordForBase.setDiet(dietResult);
            recordForBase.setAdaptation(adaptationResult);
            recordForBase.setChangeInBehavior(behaviorResult);
            recordForBase.setChatId(chatId);
            recordForBase.setDateTime(localDate);
            recordCatRepository.save(recordForBase);
            telegramBot.execute(new SendMessage(chatId, SAVE_INFORMATION.getMessage()));
            logger.info("текстовый отчет занесен в базу данных");
        }
    }

}

