package telegram.teamjob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram.teamjob.Entity.InformationForOwner;


@Repository
public interface InformationForOwnerRepository extends JpaRepository<InformationForOwner,Integer> {
}