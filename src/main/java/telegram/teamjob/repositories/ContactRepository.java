package telegram.teamjob.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import  telegram.teamjob.entity.Contact;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    List<Contact> findContactByNumberPhoneAndName(String numberPhone, String name);




}

