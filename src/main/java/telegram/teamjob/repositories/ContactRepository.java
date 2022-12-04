package telegram.teamjob.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import  telegram.teamjob.entity.Contact;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
    // удалил  Optional<Contact> findById(int id) т.к. такой метод уже есть в репозитории и он мешал при тестировании
    List<Contact> findContactByNumberPhoneAndName(String numberPhone, String name);

}

