
package lk.g41.vehicleparkingmanagementsystem.model;

public class ParkingReport {

    private String vehicleNumber;
    private String entryTime;
    private String exitTime;
    private String status;

    private double durationHours;

    public ParkingReport() {
    }

    public ParkingReport(
            String v,
            String e,
            String x,
            String s,
            double d
    ) {
        vehicleNumber = v;
        entryTime = e;
        exitTime = x;
        status = s;
        durationHours = d;
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

    public double getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(double v) {
        durationHours = v;
    }
}
