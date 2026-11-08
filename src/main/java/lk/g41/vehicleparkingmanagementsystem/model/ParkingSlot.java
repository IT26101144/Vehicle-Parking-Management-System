package lk.g41.vehicleparkingmanagementsystem.model;

public class ParkingSlot {

    private String slotId;
    private String slotType;
    private String status;
    private String vehicleNumber;

    public ParkingSlot() {
    }

    public ParkingSlot(
            String slotId,
            String slotType,
            String status,
            String vehicleNumber
    ) {
        this.slotId = slotId;
        this.slotType = slotType;
        this.status = status;
        this.vehicleNumber = vehicleNumber;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String v) {
        slotId = v;
    }

    public String getSlotType() {
        return slotType;
    }

    public void setSlotType(String v) {
        slotType = v;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String v) {
        status = v;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String v) {
        vehicleNumber = v;
    }

    public boolean isOccupied() {
        return "Occupied".equalsIgnoreCase(status);
    }

    public String getSlotNumber() {
        return slotId;
    }

    public void setSlotNumber(String v) {
        slotId = v;
    }

    public Vehicle getVehicle() {
        return null;
    }

    public void setVehicle(Vehicle v) {
        if (v != null) {
            vehicleNumber = v.getVehicleNumber();
        }
    }
}

//Dilshan update 105