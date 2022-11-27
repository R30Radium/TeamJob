package telegram.teamjob.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import telegram.teamjob.entity.PetPhoto;

import java.util.Optional;

public interface PetPhotoRepository extends PagingAndSortingRepository<PetPhoto, Long> {

    Optional<PetPhoto> findById(Long recordId);
}
