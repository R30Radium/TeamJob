package telegram.teamjob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telegram.teamjob.Entity.Record;

import java.util.LinkedList;

public interface RecordRepository extends JpaRepository<Record, Long> {

    Record findByRecordId(Long recordId);
    LinkedList<Record> findAllByChatId(Long chatId);
    Record findRecordByChatId(Long chatId);

}
