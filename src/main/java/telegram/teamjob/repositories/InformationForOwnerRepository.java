package telegram.teamjob.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram.teamjob.entity.InformationForOwner;


@Repository
public interface InformationForOwnerRepository extends JpaRepository<InformationForOwner,Integer> {
}
