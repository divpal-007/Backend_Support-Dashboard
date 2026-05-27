package com.support_dashboard.TellMe.service;

import com.support_dashboard.TellMe.dto.*;
import com.support_dashboard.TellMe.model.Escalation;
import com.support_dashboard.TellMe.repository.EscalationRepository;
import com.support_dashboard.TellMe.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService  {

    private final IncidentRepository incidentRepository;
    private final EscalationRepository escalationRepository;

    public DashboardService(IncidentRepository incidentRepository, EscalationRepository escalationRepository) {
        this.incidentRepository = incidentRepository;
        this.escalationRepository = escalationRepository;
    }

    public DashboardSummary getDashboardSummary() {
        DashboardSummary dashboardSummary = new DashboardSummary();
        dashboardSummary.setStats(buildStats());
        dashboardSummary.setEscalations(buildEscalations());
        dashboardSummary.setQueues(buildQueues());
        dashboardSummary.setPriorityIncident(buildPriorityIncident());

        return dashboardSummary;
    }

    private List<Stat>  buildStats() {
        List<Stat> stats = new ArrayList<>();

        long openIssues = incidentRepository.countByStatus("open");
        long criticalIssues = incidentRepository.countByStatus("critical");
        long escalations = incidentRepository.countByStatus("open");
        long resolvedIssues = incidentRepository.countByStatus("resolved");

//        Open issues Stats
        Stat open = new Stat();
        open.setId("open");
        open.setLabel("Open Issues");
        open.setValue(openIssues);
        open.setDelta("+12%");
        open.setTrend("up");
        open.setColor("#6366f1");
        open.setData(List.of(30,28,35,40,(int) openIssues));
        stats.add(open);

        // Critical Stats
        Stat critical = new Stat();
        critical.setId("critical");
        critical.setLabel("Critical");
        critical.setValue(criticalIssues);
        critical.setDelta("+2%");
        critical.setTrend("up");
        critical.setColor("#ef4444");
        critical.setData(List.of(2,3,2,4,(int) criticalIssues));
        stats.add(critical);

        // Escalations Stats
        Stat escalation = new Stat();
        escalation.setId("escalations");
        escalation.setLabel("Escalations");
        escalation.setValue(escalations);
        escalation.setDelta("-3");
        escalation.setTrend("down");
        escalation.setColor("#f59e0b");
        escalation.setData(List.of(8,6,7,5,(int) escalations));
        stats.add(escalation);

        //Resolved Stats
        Stat resolved = new Stat();
        resolved.setId("resolved");
        resolved.setLabel("Resolved Today");
        resolved.setValue(resolvedIssues);
        resolved.setDelta("+5");
        resolved.setTrend("up");
        resolved.setColor("#10b981");
        resolved.setData(List.of(3,5,4,6,(int) resolvedIssues));
        stats.add(resolved);

        return stats;
    }

//    Priority Incident
    private PriorityIncident buildPriorityIncident() {
        return incidentRepository.findFirstByIsPriorityTrueOrderByCreatedAtDesc()
                .map(incident -> {
                    PriorityIncident priorityIncident = new PriorityIncident();
                    priorityIncident.setId(incident.getId());
                    priorityIncident.setTitle(incident.getTitle());
                    priorityIncident.setDescription(incident.getDescription());
                    priorityIncident.setAge(formatAge(incident.getCreatedAt()));
                    priorityIncident.setImpact(incident.getImpact());
                    priorityIncident.setSeverity(incident.getSeverity());
                    priorityIncident.setImpact(incident.getImpact());

                    return priorityIncident;
                }).orElse(null); //no banner shown on dashboard
    }

    // Queue Panel
    private List<Queue> buildQueues() {
        List<Queue> queues = new ArrayList<>();
        long critical = escalationRepository.countBySeverity("critical");
        long high = escalationRepository.countBySeverity("high");
        long medium = escalationRepository.countBySeverity("medium");
        long total = critical+high+medium;

        Queue q1= new Queue();
        q1.setId("critical");
        q1.setLabel("Critical Escalations");
        q1.setCount((int) critical);
        q1.setColor("#ef4444");
        q1.setPct(total > 0 ? (int) ((critical * 100)/total) : 0);
        queues.add(q1);

        Queue q2= new Queue();
        q2.setId("high");
        q2.setLabel("High Priority");
        q2.setCount((int) high);
        q2.setColor("#f97316");
        q2.setPct(total > 0 ? (int) ((high * 100)/total) : 0);
        queues.add(q2);

        Queue q3= new Queue();
        q3.setId("medium");
        q3.setLabel("Medium Priority");
        q3.setCount((int) medium);
        q3.setColor("#f59e0b");
        q3.setPct(total > 0 ? (int) ((medium * 100)/total) : 0);
        queues.add(q3);

        return queues;
    }

    // Escalation List
    private List<Escalations> buildEscalations() {
        return escalationRepository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(this::toEscalations)
                .collect(Collectors.toList());
    }

    private Escalations toEscalations(Escalation escalations) {
        Escalations escalation = new Escalations();
        escalation.setId(escalations.getId());
        escalation.setTitle(escalations.getTitle());
        escalation.setPriority(escalations.getPriority());
        escalation.setCategory(escalations.getCategory());
        escalation.setStatus(escalations.getStatus());
        escalation.setAge(formatAge(escalations.getCreatedAt()));
        escalation.setAiConfidence(escalations.getAiConfidence());
        escalation.setAiReasoning(escalations.getAiReasoning());
        escalation.setSuggestedResolution(escalations.getSuggestedResolution());
        escalation.setAiClassified(escalation.isAiClassified());
        escalation.setAssignees(List.of()); //TODO: wire when user assignment is built
        return escalation;
    }

    // Age Formatter
    // converts LocalDateTime to "5m ago"
    private String formatAge(LocalDateTime createdAt) {
        if(createdAt == null) {
            return "unknown";
        }
        long min = ChronoUnit.MINUTES.between(createdAt, LocalDateTime.now());
        if(min < 60) {
            return min + "m ago";
        }
        long hour = ChronoUnit.HOURS.between(createdAt, LocalDateTime.now());
        if(hour < 24) {
            return hour + "h ago";
        }
        long day = ChronoUnit.DAYS.between(createdAt, LocalDateTime.now());
        return day + "d ago";
    }
}
