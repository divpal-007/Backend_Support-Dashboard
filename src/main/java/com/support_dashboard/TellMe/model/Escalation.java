package com.support_dashboard.TellMe.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "escalation")
public class Escalation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    // AI Classified - severity
    @Column(nullable = false)
    private String severity="medium";
    // AI Classified - category
    private String category;
    @Column(nullable = false)
    private String status="Open";
    private String priority;
    private Integer aiConfidence;
    private String aiReasoning; // why LLM classified it this way
    @Column(name = "suggested_resolution", columnDefinition = "TEXT")
    private String suggestedResolution;

    // what actually fixed it — engineer writes this on resolve
    // IEE learns from this for future suggestions
    @Column(columnDefinition = "TEXT")
    private String resolution; // what fixed it - IEE learns from this

    private LocalDateTime resolutionDate; // when it was resolved

    @Column(name = "ai_classified")
    private boolean aiClassified = false;

    @Column(name = "based_on_similar")
    private boolean basedOnSimilar = false;

    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getSeverity() {return severity;}
    public void setSeverity(String severity) {this.severity = severity;}

    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public String getPriority() {return priority;}
    public void setPriority(String priority) {this.priority = priority;}

    public Integer getAiConfidence() {return aiConfidence;}
    public void setAiConfidence(Integer aiConfidence) {this.aiConfidence = aiConfidence;}

    public String getAiReasoning() {return aiReasoning;}
    public void setAiReasoning(String aiReasoning) {this.aiReasoning = aiReasoning;}

    public String getResolution() {return resolution;}
    public void setResolution(String resolution) {this.resolution = resolution;}

    public LocalDateTime getResolutionDate() {return resolutionDate;}
    public void setResolutionDate(LocalDateTime resolutionDate) {this.resolutionDate = resolutionDate;}

    public boolean isAiClassified() {return aiClassified;}
    public void setAiClassified(boolean aiClassified) {this.aiClassified = aiClassified;}

    public String getCreatedBy() {return createdBy;}
    public void setCreatedBy(String createdBy) {this.createdBy = createdBy;}

    public String getSuggestedResolution() {return suggestedResolution;}
    public void setSuggestedResolution(String suggestedResolution) {this.suggestedResolution = suggestedResolution;}

    public boolean isBasedOnSimilar() {return basedOnSimilar;}
    public void setBasedOnSimilar(boolean basedOnSimilar) {this.basedOnSimilar = basedOnSimilar;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
}
