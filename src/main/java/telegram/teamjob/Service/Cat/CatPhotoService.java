package telegram.teamjob.Service.Cat;

import com.pengrad.telegrambot.model.PhotoSize;

import java.io.IOException;

public interface CatPhotoService {

    void uploadPhoto(Long recordId, PhotoSize[] photos) throws IOException;
    String getExtensions(String fileName);
}
