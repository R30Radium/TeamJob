package telegram.teamjob.repositories.Cat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram.teamjob.entity.Cat.RecordCat;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
@Repository
public interface RecordCatRepository extends JpaRepository<RecordCat, Long> {

    RecordCat findByRecordCatId(long recordCatId);
    LinkedList<RecordCat> findAllRecordCatByChatId(long chatId);
    RecordCat findRecordCatByChatId(long chatId);
    List<RecordCat> findAllRecordCatByChatIdAndDateTime(long chatId, LocalDateTime dateTime);
}
