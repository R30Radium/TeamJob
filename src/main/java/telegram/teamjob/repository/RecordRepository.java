package telegram.teamjob.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telegram.teamjob.model.Record;

import java.util.LinkedList;

public interface RecordRepository extends JpaRepository<Record, Long> {

    Record findByRecordId(Long recordId);
    LinkedList<Record> findAllByChatId(Long chatId);
    Record findRecordByChatId(Long chatId);

}
