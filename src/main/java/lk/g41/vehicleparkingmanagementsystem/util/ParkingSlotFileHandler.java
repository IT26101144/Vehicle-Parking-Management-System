package lk.g41.vehicleparkingmanagementsystem.util;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class ParkingSlotFileHandler {

    private static final Path FILE = Path.of("data", "parking_slots.txt");

    public static List<String> read() {
        try {
            return Files.exists(FILE)
                    ? new ArrayList<>(Files.readAllLines(FILE))
                    : new ArrayList<>();

        } catch (IOException e) {
            throw new RuntimeException("Unable to read parking slots", e);
        }
    }

    public static void append(String line) {
        try {
            Files.createDirectories(FILE.getParent());

            Files.writeString(
                    FILE,
                    line + System.lineSeparator(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

        } catch (IOException e) {
            throw new RuntimeException("Unable to save parking slot", e);
        }
    }

    public static void write(List<String> lines) {
        try {
            Files.createDirectories(FILE.getParent());

            Files.write(FILE, lines);

        } catch (IOException e) {
            throw new RuntimeException("Unable to update parking slots", e);
        }
    }
}

//Dilshan update 106
