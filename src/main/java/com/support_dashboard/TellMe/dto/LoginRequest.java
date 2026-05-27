package com.support_dashboard.TellMe.dto;

//DTOs - Data Transfer Object

//Spring Boot receives this JSON and automatically maps it to a LoginRequest object via @RequestBody in the controller. Jackson (the JSON library) does the mapping — field names in JSON must match field names in the class.
//Why not use the User entity directly for input?
//The User entity has id, createdAt, role — fields that make no sense during login. A DTO is the exact shape the operation needs — nothing more, nothing less.

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min=6, message = "Password must be atleast 6 characters")
    private String password;

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }
}
