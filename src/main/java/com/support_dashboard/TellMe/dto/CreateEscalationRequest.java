package com.support_dashboard.TellMe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateEscalationRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 5,max = 200,message = "Title must be 5-200 characters")
    private String title;

    private String description; //LLM uses this for better classification

    private String createdBy;


    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createBy) { this.createdBy = createBy; }
}
