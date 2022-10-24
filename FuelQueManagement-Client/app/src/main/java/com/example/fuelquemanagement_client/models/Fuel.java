package com.example.fuelquemanagement_client.models;

/**
 * The Fuel class is for the model for Fuel Object
 */
public class Fuel {

    public String id, type, date, time, stationsId, lastModified;
    public int amount;

    public Fuel() {
    }

    public Fuel(String id, String type, String date, String time, String stationsId, String lastModified, int amount) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.time = time;
        this.stationsId = stationsId;
        this.lastModified = lastModified;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStationsId() {
        return stationsId;
    }

    public void setStationsId(String stationsId) {
        this.stationsId = stationsId;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
