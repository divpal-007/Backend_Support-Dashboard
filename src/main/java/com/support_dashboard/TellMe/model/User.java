package com.support_dashboard.TellMe.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="Users")
public class User {

//    The Hibernate will create the entire table at scratch
//    CREATE TABLE users (
//    id          BIGSERIAL PRIMARY KEY,
//    name        VARCHAR(255) NOT NULL,
//    email       VARCHAR(255) NOT NULL UNIQUE,
//    password    VARCHAR(255) NOT NULL,
//    role        VARCHAR(255) NOT NULL,
//    workspace   VARCHAR(255) NOT NULL,
//    created_at  TIMESTAMP
//);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //this helps in auto increment in postgre
    private long id; //Primary Key which will be auto incremental

    @Column(nullable = false) //to customize the behavior we need just annotation column to change it
    private String name;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String workSpace;

    @Column(name="created_at") // Java convention is camelCase (createdAt), PostgreSQL convention is snake_case (created_at)
    private LocalDateTime createdDate = LocalDateTime.now(); //default value set in Java

    private String role = "Ops Manager";

    public long getId() { return id;}

    public String getEmail() { return email;}
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name;}
    public void setName(String name) { this.name = name; }

    public String getWorkSpace() { return workSpace;}
    public void setWorkSpace(String workSpace) { this.workSpace = workSpace; }

    public String getRole() { return role;}


    public String getPassword() { return password;}
    public void setPassword(String password) { this.password = password; }
}
