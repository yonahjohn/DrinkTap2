package com.example.drinktap;

import java.io.Serializable;
import java.util.Date;

/**
 * A drink contains a description, volume in oz, content and the date added.
 */
class Drink implements Serializable {
    private String description;
    private Double volume;
    private Double content;
    private Date date;

    public Drink(String description, Double volume, Double content){
        this.description = description;
        this.volume = volume;
        this.content = content;
        date = new Date();
    }

    public String getDescription() {
        return description;
    }

    public Double getVolume() {
        return volume;
    }

    public Double getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public String toString(){
        return "Description: " + description + "\n" +
                "Volume: " + volume + "\n" +
                "Content: " + content + "\n" +
                "Date: " + date.toString() + "\n";
    }
}