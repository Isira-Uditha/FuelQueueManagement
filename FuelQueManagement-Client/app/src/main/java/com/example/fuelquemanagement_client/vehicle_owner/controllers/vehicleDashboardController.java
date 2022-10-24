package com.example.fuelquemanagement_client.vehicle_owner.controllers;

import com.example.fuelquemanagement_client.constants.Constants;
import com.example.fuelquemanagement_client.models.Queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The vehicleDashboardController class facilitates the control some functionalities that are executed in VehicleOwnerDashboard class
 */
public class vehicleDashboardController {

    //According to the selected station's queue details, this counts the number of vehicles in each queue according to the vehicle type and fuel type and return the counts
    public static Map<String, Integer> getVehicleCounts(ArrayList<Queue> joinedQueues) {
        int motorBikeDieselCount = 0, motorBikePetrolCount = 0;
        int vanDieselCount = 0, vanPetrolCount = 0;
        int wheelDieselCount = 0, wheelPetrolCount = 0;
        int carDieselCount = 0, carPetrolCount = 0;
        int busDieselCount = 0, busPetrolCount = 0;
        int totalDieselCount = 0, totalPetrolCount = 0;

        for(Queue queue: joinedQueues){

            /** MOTOR_BIKE Count **/
            if(queue.getFuelType().equals(Constants.DIESEL) && queue.getVehicleType().equals(Constants.MOTOR_BIKE)){
                motorBikeDieselCount++;
            }

            if(queue.getFuelType().equals(Constants.PETROL) && queue.getVehicleType().equals(Constants.MOTOR_BIKE)){
                motorBikePetrolCount++;
            }
            /** ******* **/

            /** Van Count **/
            if(queue.getFuelType().equals(Constants.DIESEL) && queue.getVehicleType().equals(Constants.VAN)){
                vanDieselCount++;
            }

            if(queue.getFuelType().equals(Constants.PETROL) && queue.getVehicleType().equals(Constants.VAN)){
                vanPetrolCount++;
            }
            /** ******* **/

            /** Three Wheel Count **/
            if(queue.getFuelType().equals(Constants.DIESEL) && queue.getVehicleType().equals(Constants.THREE_WHEEL)){
                wheelDieselCount++;
            }

            if(queue.getFuelType().equals(Constants.PETROL) && queue.getVehicleType().equals(Constants.VAN)){
                wheelPetrolCount++;
            }
            /** ****** **/

            /** Car Count **/
            if(queue.getFuelType().equals(Constants.DIESEL) && queue.getVehicleType().equals(Constants.CAR)){
                carDieselCount++;
            }

            if(queue.getFuelType().equals(Constants.PETROL) && queue.getVehicleType().equals(Constants.CAR)){
                carPetrolCount++;
            }
            /** ****** **/

            /** Bus/Lorry Count **/
            if(queue.getFuelType().equals(Constants.DIESEL) && queue.getVehicleType().equals(Constants.BUS)){
                busDieselCount++;
            }

            if(queue.getFuelType().equals(Constants.PETROL) && queue.getVehicleType().equals(Constants.BUS)){
                busPetrolCount++;
            }
            /** ****** **/
        }

        totalDieselCount = busDieselCount+carDieselCount+wheelDieselCount+vanDieselCount+motorBikeDieselCount;
        totalPetrolCount = busPetrolCount+carPetrolCount+wheelPetrolCount+vanPetrolCount+motorBikePetrolCount;

        Map<String,Integer> vehicleCounts = new HashMap<>();

        vehicleCounts.put(Constants.MOTOR_BIKE+Constants.DIESEL, motorBikeDieselCount);
        vehicleCounts.put(Constants.MOTOR_BIKE+Constants.PETROL, motorBikePetrolCount);
        vehicleCounts.put(Constants.VAN+Constants.DIESEL, vanDieselCount);
        vehicleCounts.put(Constants.VAN+Constants.PETROL, vanPetrolCount);
        vehicleCounts.put(Constants.THREE_WHEEL+Constants.DIESEL, wheelDieselCount);
        vehicleCounts.put(Constants.THREE_WHEEL+Constants.PETROL, wheelPetrolCount);
        vehicleCounts.put(Constants.CAR+Constants.DIESEL, carDieselCount);
        vehicleCounts.put(Constants.CAR+Constants.PETROL, carPetrolCount);
        vehicleCounts.put(Constants.BUS+Constants.DIESEL, busDieselCount);
        vehicleCounts.put(Constants.BUS+Constants.PETROL, busPetrolCount);
        vehicleCounts.put(Constants.TOTAL_PETROL_COUNT, totalPetrolCount);
        vehicleCounts.put(Constants.TOTAL_DIESEL_COUNT, totalDieselCount);

        return vehicleCounts;
    }

}
