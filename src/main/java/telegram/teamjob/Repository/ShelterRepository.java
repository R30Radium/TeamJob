package telegram.teamjob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram.teamjob.Entity.Shelter;


@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Integer> {


}