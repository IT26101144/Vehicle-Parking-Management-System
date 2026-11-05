
package lk.g41.vehicleparkingmanagementsystem.util;

import lk.g41.vehicleparkingmanagementsystem.model.Payment;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentFileHandler {

    private static final String FILE_PATH = "data/payments.txt";

    public List<Payment> readAll() {
        List<Payment> payments = new ArrayList<>();
        Path path = Paths.get(FILE_PATH);

        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                return payments;
            }

            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split(",", -1);

                if (data.length == 7) {
                    Payment payment = new Payment();
                    payment.setPaymentId(data[0]);
                    payment.setRecordId(data[1]);
                    payment.setVehicleNumber(data[2]);
                    payment.setDurationHours(data[3]);
                    payment.setFee(data[4]);
                    payment.setPaymentMethod(data[5]);
                    payment.setPaymentTime(data[6]);

                    payments.add(payment);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading payment records", e);
        }

        return payments;
    }

    public void add(Payment payment) {
        List<Payment> payments = readAll();
        payments.add(payment);
        writeAll(payments);
    }

    public void writeAll(List<Payment> payments) {
        Path path = Paths.get(FILE_PATH);

        try {
            Files.createDirectories(path.getParent());

            List<String> lines = new ArrayList<>();

            for (Payment payment : payments) {
                String line = String.join(",",
                        payment.getPaymentId(),
                        payment.getRecordId(),
                        payment.getVehicleNumber(),
                        payment.getDurationHours(),
                        payment.getFee(),
                        payment.getPaymentMethod(),
                        payment.getPaymentTime()
                );

                lines.add(line);
            }

            Files.write(path, lines);

        } catch (IOException e) {
            throw new RuntimeException("Error saving payment records", e);
        }
    }
}

