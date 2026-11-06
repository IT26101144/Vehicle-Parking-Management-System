
package lk.g41.vehicleparkingmanagementsystem.model;

public class Payment {

    private String paymentId;
    private String recordId;
    private String vehicleNumber;
    private String durationHours;
    private String fee;
    private String paymentMethod;
    private String paymentTime;

    public Payment() {
    }

    public Payment(String paymentId, String vehicleNumber,
                   String durationHours, String fee,
                   String paymentMethod, String paymentTime) {
        this.paymentId = paymentId;
        this.vehicleNumber = vehicleNumber;
        this.durationHours = durationHours;
        this.fee = fee;
        this.paymentMethod = paymentMethod;
        this.paymentTime = paymentTime;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String value) {
        paymentId = value;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String value) {
        recordId = value;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String value) {
        vehicleNumber = value;
    }

    public String getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(String value) {
        durationHours = value;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String value) {
        fee = value;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String value) {
        paymentMethod = value;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String value) {
        paymentTime = value;
    }
}
