package com.support_dashboard.TellMe.dto;

public class PriorityIncident {
    private Long id;
    private String title;
    private String severity;
    private String impact;
    private String age; //"5m ago" - computed from createdAt
    private String description;
    private Integer aiConfidence;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getSeverity() {return severity;}
    public void setSeverity(String severity) {this.severity = severity;}

    public String getImpact() {return impact;}
    public void setImpact(String impact) {this.impact = impact;}

    public String getAge() {return age;}
    public void setAge(String age) {this.age = age;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public Integer getAiConfidence() {return aiConfidence;}

    public void setAiConfidence(Integer aiConfidence) {this.aiConfidence = aiConfidence;}
}
