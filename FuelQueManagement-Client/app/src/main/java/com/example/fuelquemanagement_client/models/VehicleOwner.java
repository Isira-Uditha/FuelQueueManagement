package com.example.fuelquemanagement_client.models;

/**
 * The VehicleOwner class is for the model for VehicleOwner Object
 */
public class VehicleOwner {

    private int id;
    private String fName, username, password;

    public VehicleOwner() {
    }

    public VehicleOwner(int id, String fName, String username, String password) {
        this.id = id;
        this.fName = fName;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getfName() {
        return fName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void seteId(int id) {
        this.id = id;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
