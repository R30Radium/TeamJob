package telegram.teamjob.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram.teamjob.entity.Record;
import java.util.LinkedList;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    Record findByRecordId(Long recordId);
    LinkedList<Record> findAllByChatId(Long chatId);
    Record findRecordByChatId(Long chatId);
}
