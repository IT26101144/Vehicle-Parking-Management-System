package lk.g41.vehicleparkingmanagementsystem.model;

public class Car extends Vehicle {

    public Car() {
        super();
    }

    public Car(String vehicleId, String vehicleNumber, String ownerName) {
        super(vehicleId, vehicleNumber, "Car", ownerName);
    }
}