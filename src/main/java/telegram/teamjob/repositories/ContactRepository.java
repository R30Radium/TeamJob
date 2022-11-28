package telegram.teamjob.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import  telegram.teamjob.model.Contact;



@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {






}

