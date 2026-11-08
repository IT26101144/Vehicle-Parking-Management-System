
package lk.g41.vehicleparkingmanagementsystem.service;

import lk.g41.vehicleparkingmanagementsystem.model.Payment;
import lk.g41.vehicleparkingmanagementsystem.model.ParkingRecord;
import lk.g41.vehicleparkingmanagementsystem.util.PaymentFileHandler;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PaymentService {

    private final EntryExitService entryExitService;

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PaymentService(EntryExitService entryExitService) {
        this.entryExitService = entryExitService;
    }

    private String clean(String value) {
        return value == null ? "" : value.trim().replace(",", " ");
    }

    private String toLine(Payment p) {
        return String.join(",",
                clean(p.getPaymentId()),
                clean(p.getRecordId()),
                clean(p.getVehicleNumber()),
                clean(p.getDurationHours()),
                clean(p.getFee()),
                clean(p.getPaymentMethod()),
                clean(p.getPaymentTime()));
    }

    private Payment parse(String line) {
        String[] p = line.split(",", -1);

        if (p.length == 7) {
            Payment payment = new Payment(
                    p[0], p[2], p[3], p[4], p[5], p[6]);
            payment.setRecordId(p[1]);
            return payment;
        }

        if (p.length == 6) {
            return new Payment(
                    p[0], p[1], p[2], p[3], p[4], p[5]);
        }

        return null;
    }

    public List<Payment> all() {
        List<Payment> payments = new ArrayList<>();

        for (String line : PaymentFileHandler.read()) {
            Payment payment = parse(line);
            if (payment != null) {
                payments.add(payment);
            }
        }

        return payments;
    }

    public List<ParkingRecord> getUnpaidExitedRecords() {
        Set<String> paidRecordIds = new HashSet<>();

        for (Payment payment : all()) {
            if (payment.getRecordId() != null
                    && !payment.getRecordId().isBlank()) {
                paidRecordIds.add(payment.getRecordId());
            }
        }

        List<ParkingRecord> unpaid = new ArrayList<>();

        for (ParkingRecord record : entryExitService.all()) {
            if ("Exited".equalsIgnoreCase(record.getStatus())
                    && record.getExitTime() != null
                    && !record.getExitTime().isBlank()
                    && !paidRecordIds.contains(record.getRecordId())) {
                unpaid.add(record);
            }
        }

        return unpaid;
    }

    public BigDecimal calculateFee(ParkingRecord record) {
        LocalDateTime entry =
                LocalDateTime.parse(record.getEntryTime(), FORMAT);
        LocalDateTime exit =
                LocalDateTime.parse(record.getExitTime(), FORMAT);

        long minutes = Duration.between(entry, exit).toMinutes();

        if (minutes < 0) {
            throw new IllegalArgumentException(
                    "Exit time cannot be before entry time.");
        }

        long extraHours = minutes <= 60
                ? 0
                : (long) Math.ceil((minutes - 60) / 60.0);

        return BigDecimal.valueOf(100 + extraHours * 30)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public boolean add(String recordId, String paymentMethod) {
        if (recordId == null || recordId.isBlank()
                || paymentMethod == null || paymentMethod.isBlank()) {
            return false;
        }

        ParkingRecord record = entryExitService.find(recordId);

        if (record == null
                || !"Exited".equalsIgnoreCase(record.getStatus())
                || record.getExitTime() == null
                || record.getExitTime().isBlank()) {
            return false;
        }

        for (Payment existing : all()) {
            if (recordId.equals(existing.getRecordId())) {
                return false;
            }
        }

        try {
            LocalDateTime entry =
                    LocalDateTime.parse(record.getEntryTime(), FORMAT);
            LocalDateTime exit =
                    LocalDateTime.parse(record.getExitTime(), FORMAT);

            long minutes = Duration.between(entry, exit).toMinutes();

            if (minutes < 0) {
                return false;
            }

            BigDecimal duration =
                    BigDecimal.valueOf(minutes)
                            .divide(BigDecimal.valueOf(60), 2,
                                    RoundingMode.HALF_UP);

            Payment payment = new Payment();
            payment.setPaymentId("PAY" + System.currentTimeMillis());
            payment.setRecordId(recordId);
            payment.setVehicleNumber(record.getVehicleNumber());
            payment.setDurationHours(duration.toPlainString());
            payment.setFee(calculateFee(record).toPlainString());
            payment.setPaymentMethod(paymentMethod.trim());
            payment.setPaymentTime(LocalDateTime.now().format(FORMAT));

            PaymentFileHandler.append(toLine(payment));
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(String paymentId) {
        List<String> lines = PaymentFileHandler.read();

        boolean removed = lines.removeIf(line -> {
            Payment payment = parse(line);
            return payment != null
                    && payment.getPaymentId().equals(paymentId);
        });

        if (removed) {
            PaymentFileHandler.write(lines);
        }

        return removed;
    }

    public BigDecimal total() {
        BigDecimal total = BigDecimal.ZERO;

        for (Payment payment : all()) {
            try {
                total = total.add(new BigDecimal(payment.getFee()));
            } catch (Exception ignored) {
            }
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public Payment find(String paymentId) {
        return all().stream()
                .filter(p -> p.getPaymentId().equals(paymentId))
                .findFirst()
                .orElse(null);
    }
}

