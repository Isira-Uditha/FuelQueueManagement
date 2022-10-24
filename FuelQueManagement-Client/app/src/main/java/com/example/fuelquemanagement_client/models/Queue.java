package com.example.fuelquemanagement_client.models;

import java.io.Serializable;

/**
 * The Queue class is for the model for Queue Object
 */
public class Queue implements Serializable {

    public String id, vehicleType, vehicleOwner, fuelType, stationsId, arrivalTime, departTime;

    public Queue() {
    }

    public Queue(String id, String vehicleType, String vehicleOwner, String fuelType, String stationsId) {
        this.id = id;
        this.vehicleType = vehicleType;
        this.vehicleOwner = vehicleOwner;
        this.fuelType = fuelType;
        this.stationsId = stationsId;
    }

    public Queue(String id, String vehicleType, String vehicleOwner, String fuelType, String stationsId, String arrivalTime, String departTime) {
        this.id = id;
        this.vehicleType = vehicleType;
        this.vehicleOwner = vehicleOwner;
        this.fuelType = fuelType;
        this.stationsId = stationsId;
        this.arrivalTime = arrivalTime;
        this.departTime = departTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleOwner() {
        return vehicleOwner;
    }

    public void setVehicleOwner(String vehicleOwner) {
        this.vehicleOwner = vehicleOwner;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getStationsId() {
        return stationsId;
    }

    public void setStationsId(String stationsId) {
        this.stationsId = stationsId;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }
}
