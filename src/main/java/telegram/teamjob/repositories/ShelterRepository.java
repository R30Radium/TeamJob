package telegram.teamjob.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram.teamjob.entity.Shelter;


@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Integer> {


}