package lk.g41.vehicleparkingmanagementsystem.model;

public class Van extends Vehicle {

    public Van() {
        super();
    }

    public Van(String vehicleId, String vehicleNumber, String ownerName) {
        super(vehicleId, vehicleNumber, "Van", ownerName);
    }
}