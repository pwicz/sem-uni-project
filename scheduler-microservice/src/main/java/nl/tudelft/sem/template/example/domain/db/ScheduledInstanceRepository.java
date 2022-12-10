package nl.tudelft.sem.template.example.domain.db;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduledInstanceRepository extends JpaRepository<ScheduledInstance, Long> {
    List<ScheduledInstance> findAllByJobId(long jobId);
}
