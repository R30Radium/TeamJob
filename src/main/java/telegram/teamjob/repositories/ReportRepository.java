package telegram.teamjob.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram.teamjob.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}