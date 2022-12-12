package telegram.teamjob.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram.teamjob.entity.Record;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    Record findRecordByRecordId(long recordId);
    LinkedList<Record> findAllRecordByChatId(long chatId);
    Record findRecordByChatId(long chatId);
    List<Record> findAllRecordByChatIdAndDateTime(long chatId, LocalDateTime dateTime);
}