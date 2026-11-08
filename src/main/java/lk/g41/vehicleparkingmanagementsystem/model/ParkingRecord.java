
package lk.g41.vehicleparkingmanagementsystem.model;

public class ParkingRecord {

    private String recordId;
    private String vehicleNumber;
    private String entryTime;
    private String exitTime;
    private String status;

    public ParkingRecord() {
    }

    public ParkingRecord(
            String id,
            String number,
            String entry,
            String exit,
            String status
    ) {
        recordId = id;
        vehicleNumber = number;
        entryTime = entry;
        exitTime = exit;
        this.status = status;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String v) {
        recordId = v;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String v) {
        vehicleNumber = v;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String v) {
        entryTime = v;
    }

    public String getExitTime() {
        return exitTime;
    }

    public void setExitTime(String v) {
        exitTime = v;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String v) {
        status = v;
    }
}
