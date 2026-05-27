package com.support_dashboard.TellMe.dto;

public class Queue {
    private String id;
    private String label;
    private Integer count;
    private String color;
    private Integer pct;

    public Integer getCount() {return count;}
    public void setCount(Integer count) {this.count = count;}

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getLabel() {return label;}
    public void setLabel(String label) {this.label = label;}

    public String getColor() {return color;}
    public void setColor(String color) {this.color = color;}

    public Integer getPct() {return pct;}
    public void setPct(Integer pct) {this.pct = pct;}
}
