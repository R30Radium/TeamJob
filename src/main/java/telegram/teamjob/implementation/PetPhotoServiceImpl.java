<<<<<<< HEAD
<<<<<<<< HEAD:src/main/java/telegram/teamjob/implementation/PetPhotoServiceImpl.java
package telegram.teamjob.implementation;
========
package telegram.teamjob.Service;
>>>>>>>> Bagrin-feature:src/main/java/telegram/teamjob/service/PetPhotoServiceImpl.java
=======
package telegram.teamjob.implementation;
>>>>>>> Bagrin-feature

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
<<<<<<< HEAD
=======
import com.pengrad.telegrambot.TelegramBot;
>>>>>>> Bagrin-feature
import com.pengrad.telegrambot.model.PhotoSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
<<<<<<< HEAD
import telegram.teamjob.service.PetPhotoService;
=======
import telegram.teamjob.Service.PetPhotoService;
>>>>>>> Bagrin-feature
import telegram.teamjob.entity.PetPhoto;
import telegram.teamjob.repositories.PetPhotoRepository;
import telegram.teamjob.repositories.RecordRepository;
import telegram.teamjob.entity.Record;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
<<<<<<< HEAD
=======

>>>>>>> Bagrin-feature
@Service
public class PetPhotoServiceImpl implements PetPhotoService {
    Logger logger = LoggerFactory.getLogger(PetPhotoServiceImpl.class);

    @Value("${path.to.photos.folder}")
    private String photoDis;
    @Value("${telegram.bot.token}")
    private String value;
    private final PetPhotoRepository petPhotoRepository;
<<<<<<< HEAD

=======
>>>>>>> Bagrin-feature
    private final RecordRepository recordRepository;

    public PetPhotoServiceImpl(PetPhotoRepository petPhotoRepository, RecordRepository recordRepository) {
        this.petPhotoRepository = petPhotoRepository;
        this.recordRepository = recordRepository;
    }
    @Override
    public void uploadPhoto(Long recordId, PhotoSize[] photos) throws IOException {
        logger.info("Was invoked method for upload avatar");
<<<<<<< HEAD
<<<<<<<< HEAD:src/main/java/telegram/teamjob/implementation/PetPhotoServiceImpl.java
        Record record = recordRepository.findRecordByRecordId(recordId);
        String fileId = photos[0].fileId();
        String fileName = photos[0].fileUniqueId();
        System.out.println(fileId);
        System.out.println(fileName);
========
=======
>>>>>>> Bagrin-feature
        Record record = recordRepository.findByRecordId(recordId);
        if (record != null) {
            String fileId = photos[0].fileId();
            String fileName = photos[0].fileUniqueId();
            System.out.println(fileId);
            System.out.println(fileName);
<<<<<<< HEAD
>>>>>>>> Bagrin-feature:src/main/java/telegram/teamjob/service/PetPhotoServiceImpl.java
=======
>>>>>>> Bagrin-feature

            String botToken = value;
            URL url = new URL("https://api.telegram.org/bot" + botToken + "/" + "getFile?file_id=" + fileId);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String getFileResponse = br.readLine();

            JsonElement jResult = JsonParser.parseString(getFileResponse);
            JsonObject path = jResult.getAsJsonObject().getAsJsonObject("result");
            String file_path = path.get("file_path").getAsString();
            System.out.println(file_path);

            Path filePath = Path.of(photoDis, record.getRecordId() + "." +
                    getExtensions(Objects.requireNonNull(fileName)));
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            try (
                    InputStream is = new URL("https://api.telegram.org/file/bot" + botToken + "/" + file_path).openConnection().getInputStream();
                    OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                    BufferedInputStream bis = new BufferedInputStream(is, 480);
                    BufferedOutputStream bos = new BufferedOutputStream(os, 480);
            ) {
                bis.transferTo(bos);
            }
            PetPhoto petPhoto = new PetPhoto();
            petPhoto.setRecord(record);
            petPhoto.setFilePath(filePath.toString());
            petPhoto.setFileSize(photos[0].fileSize());
            br.close();
            petPhotoRepository.save(petPhoto);
            logger.info("Photo was saved");
        } else {
            logger.info("Record not found");
        }
<<<<<<< HEAD
<<<<<<<< HEAD:src/main/java/telegram/teamjob/implementation/PetPhotoServiceImpl.java
        PetPhoto petPhoto = new PetPhoto();
        //petPhoto.setRecord(record);
        petPhoto.setFilePath(filePath.toString());
        petPhoto.setFileSize(photos[0].fileSize());
        br.close();
        petPhotoRepository.save(petPhoto);
========
>>>>>>>> Bagrin-feature:src/main/java/telegram/teamjob/service/PetPhotoServiceImpl.java
=======
>>>>>>> Bagrin-feature
    }
    @Override
    public String getExtensions(String fileName) {
        logger.debug("Requesting file name:{}", fileName);
        logger.info("Was invoked method for get extensions");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


<<<<<<< HEAD



=======
>>>>>>> Bagrin-feature
}

