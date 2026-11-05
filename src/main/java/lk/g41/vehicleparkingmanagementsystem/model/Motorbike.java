package lk.g41.vehicleparkingmanagementsystem.model;

public class Motorbike extends Vehicle {

    public Motorbike() {
        super();
    }

    public Motorbike(String vehicleId, String vehicleNumber, String ownerName) {
        super(vehicleId, vehicleNumber, "Motorbike", ownerName);
    }
}