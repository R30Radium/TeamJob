package telegram.teamjob.repositories.Cat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram.teamjob.entity.Contact;
import telegram.teamjob.entity.Cat.ContactCat;

import java.util.List;

@Repository
public interface ContactCatRepository extends JpaRepository<ContactCat, Integer> {
    List<Contact> findContactByNumberPhoneAndName(String numberPhone, String name);
}
