package telegram.teamjob.repositories.Cat;

import org.springframework.data.jpa.repository.JpaRepository;
import telegram.teamjob.entity.Cat.ShelterForCat;

public interface ShelterForCatRepository extends JpaRepository<ShelterForCat, Integer> {
}
