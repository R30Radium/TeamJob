package telegram.teamjob.Service;

import com.pengrad.telegrambot.model.Update;

public interface ContactService {
    void saveContact(Update update);
    void getAllContactsForVolunteer(Update update);
    void deleteAllContacts(Update update);
}
