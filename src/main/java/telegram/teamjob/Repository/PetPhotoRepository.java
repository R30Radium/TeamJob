package telegram.teamjob.Repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import telegram.teamjob.Entity.PetPhoto;

import java.util.Optional;

public interface PetPhotoRepository extends PagingAndSortingRepository<PetPhoto, Long> {

    Optional<PetPhoto> findById(Long recordId);
}
