package com.support_dashboard.TellMe.dto;

import lombok.Data;

@Data
public class CreateTicketRequest {
    private String title;
    private String description;
    private String userName;
    private String email;
}