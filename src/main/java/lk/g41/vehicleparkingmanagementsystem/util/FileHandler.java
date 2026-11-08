package lk.g41.vehicleparkingmanagementsystem.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileHandler {

    private static final String VEHICLE_FILE = "data/vehicles.txt";

    // Add data to the file
    public static void appendVehicle(String data) {

        try {
            Path path = Paths.get(VEHICLE_FILE);

            Files.createDirectories(path.getParent());

            Files.writeString(
                    path,
                    data + System.lineSeparator(),
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.APPEND
            );

        } catch (IOException e) {
            System.out.println("Error writing to vehicle file: " + e.getMessage());
        }
    }

    // Read all vehicle data
    public static List<String> readVehicles() {

        try {
            Path path = Paths.get(VEHICLE_FILE);

            if (!Files.exists(path)) {
                return List.of();
            }

            return Files.readAllLines(path);

        } catch (IOException e) {
            System.out.println("Error reading vehicle file: " + e.getMessage());
            return List.of();
        }
    }

    public static void writeVehicles(List<String> vehicles) {

        try {
            Path path = Paths.get(VEHICLE_FILE);

            Files.createDirectories(path.getParent());

            Files.write(
                    path,
                    vehicles
            );

        } catch (IOException e) {
            System.out.println("Error writing vehicle file: " + e.getMessage());
        }
    }
}