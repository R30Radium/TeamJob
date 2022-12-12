package telegram.teamjob.service;

import com.pengrad.telegrambot.model.PhotoSize;
import java.io.IOException;

public interface PetPhotoService {
    void uploadPhoto(Long recordId, PhotoSize[] photos) throws IOException;
    String getExtensions(String fileName);
}
