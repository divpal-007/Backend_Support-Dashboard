package com.support_dashboard.TellMe.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;
    private String description;

    @Column(nullable = false)
    private String severity;
    private String impact;

    // AI confidence score 0-100
    // how sure LLM is about its classification
    private Integer aiConfidence;

    // marks this as the top priority incident
    // only one should be true at a time
    // PriorityBanner reads this field
    @Column(name = "is_priority")
    private boolean isPriority = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private String status = "open";

    @Column(columnDefinition = "TEXT")
    private String resolution;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    public long getId(){ return id; }
    public void setId(long id){ this.id = id; }

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getSeverity() {return severity;}
    public void setSeverity(String severity) {this.severity = severity;}

    public String getImpact() {return impact;}
    public void setImpact(String impact) {this.impact = impact;}

    public Integer getAiConfidence() {return aiConfidence;}
    public void setAiConfidence(Integer aiConfidence) {this.aiConfidence = aiConfidence;}

    public boolean isPriority() {return isPriority;}
    public void setPriority(boolean isPriority) {this.isPriority = isPriority;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
}
