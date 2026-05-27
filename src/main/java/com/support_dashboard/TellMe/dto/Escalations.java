package com.support_dashboard.TellMe.dto;

import java.util.List;

public class Escalations {
    private Long id;
    private String title;
    private String severity; // badge color
    private String category; // tag label
    private String age; //5m ago
    private String status; //open,resolved
    private String priority;
    private String description;
    private Integer aiConfidence; //shown on row
    private String aiReasoning; //tooltip on hover
    private String suggestedResolution; //IEE Suggestion
    private Boolean aiClassified; // show AI badge
    private List<String> assignees; //initials list

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getSeverity() {return severity;}
    public void setSeverity(String severity) {this.severity = severity;}

    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}

    public String getAge() {return age;}
    public void setAge(String age) {this.age = age;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public String getPriority() {return priority;}
    public void setPriority(String priority) {this.priority = priority;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public Integer getAiConfidence() {return aiConfidence;}
    public void setAiConfidence(Integer aiConfidence) {this.aiConfidence = aiConfidence;}

    public String getSuggestedResolution() {return suggestedResolution;}
    public void setSuggestedResolution(String suggestedResolution) {this.suggestedResolution = suggestedResolution;}

    public Boolean isAiClassified() {return aiClassified;}
    public void setAiClassified(Boolean aiClassified) {this.aiClassified = aiClassified;}

    public List<String> getAssignees() {return assignees;}
    public void setAssignees(List<String> assignees) {this.assignees = assignees;}

    public String getAiReasoning() {return aiReasoning;}
    public void setAiReasoning(String aiReasoning) {this.aiReasoning = aiReasoning;}
}
