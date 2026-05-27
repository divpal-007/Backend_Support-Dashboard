package com.support_dashboard.TellMe.repository;

import com.support_dashboard.TellMe.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository  extends JpaRepository<Incident,Long> {
    // fetch the one marked as priority - for priority Banner
    Optional<Incident> findFirstByIsPriorityTrueOrderByCreatedAtDesc();

    // count by severity - for stats and queue panel
    long countBySeverity(String severity);

    // count by status - for open issues stat
    long countByStatus(String status);

    // recent 5 Incidents for activity feed
    List<Incident> findTop5ByOrderByCreatedAtDesc();

    List<Incident> findBySeverityAndStatusOrderByCreatedAtDesc(String severity, String status);

}
