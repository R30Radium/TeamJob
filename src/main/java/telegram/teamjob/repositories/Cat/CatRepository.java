package telegram.teamjob.repositories.Cat;

import org.springframework.data.jpa.repository.JpaRepository;
import telegram.teamjob.entity.Cat.Cat;

public interface CatRepository extends JpaRepository<Cat, Long> {
}
