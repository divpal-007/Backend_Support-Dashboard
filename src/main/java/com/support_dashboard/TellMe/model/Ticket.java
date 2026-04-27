package com.support_dashboard.TellMe.model;

import com.support_dashboard.TellMe.dto.AIResponse;
import lombok.*;
import jakarta.persistence.*;

@Entity  //create table in DB
@Data //getters,setters,toString
@NoArgsConstructor //empty constructor
@AllArgsConstructor //full constructor
public class Ticket {
    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            //ticket id

    private String title;       //ticket title
    private String description; //ticket description
    private String userName; //ticket user
    private String email; //ticket user email
    private String status; //ticket status
    private String createdAt; //ticket creation date
    private String avatar; //ticket avatar

    private String category;    //AI Based Classification variables
    private String priority;
    private String sentiment;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {return category;}

    public void setCategory(String category){this.category = category;}

    public String getPriority() { return priority;}

    public void setPriority(String priority){this.priority = priority;}

    public String getSentiment() {return sentiment;}

    public void setSentiment(String sentiment){this.sentiment = sentiment;}

    public String getStatus() {return status;}

    public void setStatus(String status){this.status=status;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title=title;}

    public String getUserName() {return userName;}

    public void setUserName(String userName) {this.userName=userName;}

    public String getCreatedAt() {return createdAt;}

    public void setCreatedAt(String createdAt) {this.createdAt=createdAt;}

    public String getAvatar() {return avatar;}

    public void setAvatar(String avatar) {this.avatar=avatar;}

    public Long getId() {return id;}

    public void setId(Long id) {this.id=id;}
}
