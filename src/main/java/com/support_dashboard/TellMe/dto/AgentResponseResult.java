package com.support_dashboard.TellMe.dto;

import lombok.*;

@Data
public class AgentResponseResult {
    private String category;
    private String priority;
    private String reasoning;
    private String severity;
    private Integer confidence;
    private boolean aiClassified;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }

    public Integer getConfidence() {
        return confidence;
    }
    public void setConfidence(Integer confidence) {this.confidence = confidence;}

    public String getSeverity() {return severity;}
    public void setSeverity(String severity) {this.severity = severity;}

    public boolean isAiClassified() {return aiClassified;}
    public void setAiClassified(boolean aiClassified) {this.aiClassified = aiClassified;}

    //    Safe Fallback when Ai Agent fails
    public static AgentResponseResult defaultResult(){
        AgentResponseResult result = new AgentResponseResult();
        result.setConfidence(50);
        result.setSeverity("medium");
        result.setCategory("Application");
        result.setPriority("P3");
        result.setReasoning("Auto-Classified - manual Review Recommended");
        result.setAiClassified(false);
        return result;
    }
}
