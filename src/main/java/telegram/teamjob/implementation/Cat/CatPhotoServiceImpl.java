package telegram.teamjob.implementation.Cat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pengrad.telegrambot.model.PhotoSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import telegram.teamjob.Service.Cat.CatPhotoService;
import telegram.teamjob.entity.Cat.CatPhoto;
import telegram.teamjob.entity.Cat.RecordCat;
import telegram.teamjob.repositories.Cat.CatPhotoRepository;
import telegram.teamjob.repositories.Cat.RecordCatRepository;


import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

public class CatPhotoServiceImpl implements CatPhotoService {
    Logger logger = LoggerFactory.getLogger(CatPhotoServiceImpl.class);

    @Value("${path.to.photos.folder}")
    private String photoDis;
    @Value("${telegram.bot.token}")
    private String value;
    private final CatPhotoRepository catPhotoRepository;
    private final RecordCatRepository recordCatRepository;

    public CatPhotoServiceImpl(CatPhotoRepository catPhotoRepository, RecordCatRepository recordCatRepository) {
        this.catPhotoRepository = catPhotoRepository;
        this.recordCatRepository = recordCatRepository;
    }
    @Override
    public void uploadPhoto(Long recordId, PhotoSize[] photos) throws IOException {
        logger.info("Was invoked method for upload avatar");
        RecordCat recordCat = recordCatRepository.findByRecordCatId(recordId);
        if (recordCat != null) {
            String fileId = photos[0].fileId();
            String fileName = photos[0].fileUniqueId();
            System.out.println(fileId);
            System.out.println(fileName);

            String botToken = value;
            URL url = new URL("https://api.telegram.org/bot" + botToken + "/" + "getFile?file_id=" + fileId);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String getFileResponse = br.readLine();

            JsonElement jResult = JsonParser.parseString(getFileResponse);
            JsonObject path = jResult.getAsJsonObject().getAsJsonObject("result");
            String file_path = path.get("file_path").getAsString();
            System.out.println(file_path);

            Path filePath = Path.of(photoDis, recordCat.getRecordCatId() + "." +
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
            CatPhoto catPhoto = new CatPhoto();
            catPhoto.setRecordCat(recordCat);
            catPhoto.setFilePath(filePath.toString());
            catPhoto.setFileSize(photos[0].fileSize());
            br.close();

            catPhotoRepository.save(catPhoto);
            logger.info("Photo was saved");
        } else {
            logger.info("Record not found");
        }
    }
    @Override
    public String getExtensions(String fileName) {
        logger.debug("Requesting file name:{}", fileName);
        logger.info("Was invoked method for get extensions");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }




}
