package telegram.teamjob.repositories.Cat;

import org.springframework.data.repository.PagingAndSortingRepository;
import telegram.teamjob.entity.Cat.CatPhoto;
import telegram.teamjob.entity.PetPhoto;

import java.util.Optional;

public interface CatPhotoRepository extends PagingAndSortingRepository<CatPhoto, Long> {
    Optional<CatPhoto> findById(Long recordId);

    CatPhoto findCatPhotoByCatPhotosId(Long catPhotoId);
}
