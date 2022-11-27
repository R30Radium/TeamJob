package telegram.teamjob.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telegram.teamjob.entity.Record;

import java.util.LinkedList;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    Record findByRecordId(Long recordId);
    LinkedList<Record> findAllByChatId(Long chatId);
    Record findRecordByChatId(Long chatId);

}
