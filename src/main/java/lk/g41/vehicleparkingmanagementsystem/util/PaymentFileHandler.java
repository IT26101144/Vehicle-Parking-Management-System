package lk.g41.vehicleparkingmanagementsystem.util;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class PaymentFileHandler {

    private static final Path FILE = Path.of("data", "payments.txt");

    public static List<String> read() {
        try {
            return Files.exists(FILE)
                    ? new ArrayList<>(Files.readAllLines(FILE))
                    : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void append(String l) {
        try {
            Files.createDirectories(FILE.getParent());

            Files.writeString(
                    FILE,
                    l + System.lineSeparator(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(List<String> l) {
        try {
            Files.createDirectories(FILE.getParent());

            Files.write(FILE, l);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
