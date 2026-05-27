package com.support_dashboard.TellMe.dto;

import java.util.List;

public class DashboardSummary {
    private List<Stat> stats;
    private PriorityIncident priorityIncident;
    private List<Queue> queues;
    private List<Escalations> escalations;

    public List<Stat> getStats() {return stats;}
    public void setStats(List<Stat> stats) {this.stats = stats;}

    public PriorityIncident getPriorityIncident() {return priorityIncident;}
    public void setPriorityIncident(PriorityIncident priorityIncident) {this.priorityIncident = priorityIncident;}

    public List<Queue> getQueues() {return queues;}
    public void setQueues(List<Queue> queues) {this.queues = queues;}

    public List<Escalations> getEscalations() {return escalations;}
    public void setEscalations(List<Escalations> escalations) {this.escalations = escalations;}
}
