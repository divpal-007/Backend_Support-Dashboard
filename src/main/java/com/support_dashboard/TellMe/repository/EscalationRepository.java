package com.support_dashboard.TellMe.repository;

import com.support_dashboard.TellMe.model.Escalation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EscalationRepository extends JpaRepository<Escalation,Long> {

    // recent 10 for dashboard list
    List<Escalation> findTop10ByOrderByCreatedAtDesc();

    // count by severity - for queue panel
    long countBySeverity(String severity);

    // count by status - for stats
    long countByStatus(String status);

    // filter by status - for escalations page later
    List<Escalation> findByStatusOrderByCreatedAtDesc(String status);

    List<Escalation> findByCategoryAndStatusOrderByCreatedAtDesc(String category,String status);

//    find all AI classified tickets - for IEE analytics
    List<Escalation> findByAiClassifiedTrue();
}
