package com.example.fuelquemanagement_client.models;

/**
 * The StationOwner class is for the model for StationOwner Object
 */
public class StationOwner {

    private int id;
    private String fName, username,location, stationName, password;

    public StationOwner() {
    }

    public StationOwner(int id, String fName, String username, String location, String stationName, String password) {
        this.id = id;
        this.fName = fName;
        this.username = username;
        this.location = location;
        this.stationName = stationName;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
