package telegram.teamjob.repositories.Cat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram.teamjob.entity.Cat.VolunteerCat;

@Repository
public interface VolunteerCatRepository extends JpaRepository<VolunteerCat, Long> {
}
