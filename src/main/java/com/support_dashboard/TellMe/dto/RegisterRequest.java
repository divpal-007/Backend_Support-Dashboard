package com.support_dashboard.TellMe.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
//    @NotBlank is the strictest — always use it for text fields you require.
//    Without @Valid the annotations on the DTO are ignored completely

    @NotBlank(message = "Name is required")
    @Size(min = 2, message = "Name must be at least 2 characters")
    private String name;

    @NotBlank(message ="Email is required")
    @Email(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Workspace name is required")
    @Size(min = 2, message = "Workspace name must be at least 2 characters")
    private String workspace;

    public String getName() { return name; }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public String getWorkspace() { return workspace; }

    public void setName(String name) { this.name = name; }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public void setWorkspace(String workspace) { this.workspace = workspace; }
}
