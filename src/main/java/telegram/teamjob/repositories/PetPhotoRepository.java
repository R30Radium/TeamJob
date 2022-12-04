package telegram.teamjob.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import telegram.teamjob.entity.PetPhoto;

import java.util.Optional;
@Repository
public interface
PetPhotoRepository extends PagingAndSortingRepository<PetPhoto, Long> {
}
