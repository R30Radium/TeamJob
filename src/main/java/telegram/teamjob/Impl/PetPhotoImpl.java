package telegram.teamjob.Impl;


import com.pengrad.telegrambot.model.PhotoSize;
import java.io.IOException;

public interface PetPhotoImpl {
    void uploadPhoto(Long recordId, PhotoSize[] photos) throws IOException;
    String getExtensions(String fileName);

}