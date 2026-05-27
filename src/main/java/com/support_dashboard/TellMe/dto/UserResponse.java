package com.support_dashboard.TellMe.dto;

public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String workspace;
    private String initials;
    // UserResponse — keeping password off the wire
// learned this the hard way
//    private String password;
    public long getId() { return id; }
    public void setId(Long id) { this.id = id;}

    public String getName() { return name; }
    public void setName(String name) { this.name = name;}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getWorkspace() { return workspace; }
    public void setWorkspace(String workspace) { this.workspace = workspace; }

    public String getInitials() { return initials; }
    public void setInitials(String initials) { this.initials = initials; }

//    Three things to notice:
//1. No password — the most important thing. Filtered out entirely. The UserResponse class doesn't even have a password field — it's structurally impossible to leak it.
//2. initials field — this doesn't exist in the database. It's computed in AuthService:
//    DTOs can carry computed fields — data derived from the entity but not stored. The frontend needs initials for the avatar — you compute it once on the backend rather than making React figure it out.
//3. createdAt not included — React doesn't need it for auth. You consciously leave it out.
}
