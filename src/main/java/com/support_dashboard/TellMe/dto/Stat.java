package com.support_dashboard.TellMe.dto;

import java.util.List;

public class Stat {
    private String id;
    // display label — "Open Issues", "Critical" etc

    private String label;
    // the number shown — Long to handle large counts

    private Object value;      // can be int or String like "94%"
    // change indicator — "+12%" or "-3"

    private String delta;
    // "up" or "down" — drives arrow direction in UI

    private String trend;      // up or down
    // hex color — matches design tokens in variables.css

    private String color;

    // sparkline data — last 5 data points
    // drives the small chart in each stat card
    private List<Integer> data;

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getLabel() {return label;}
    public void setLabel(String label) {this.label = label;}

    public Object getValue() {return value;}
    public void setValue(Object value) {this.value = value;}

    public String getDelta() {return delta;}
    public void setDelta(String delta) {this.delta = delta;}

    public String getTrend() {return trend;}
    public void setTrend(String trend) {this.trend = trend;}

    public String getColor() {return color;}
    public void setColor(String color) {this.color = color;}

    public List<Integer> getData() {return data;}
    public void setData(List<Integer> data) {this.data = data;}
}
