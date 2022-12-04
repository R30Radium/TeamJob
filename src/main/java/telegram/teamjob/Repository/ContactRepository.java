package telegram.teamjob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram.teamjob.Entity.Contact;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    List<Contact> findById(int id);

    List<com.pengrad.telegrambot.model.Contact> findContactByNumberPhoneAndName(String numberPhone, String name);

    List<Contact> addContact(int id);


}
