
package lk.g41.vehicleparkingmanagementsystem.util;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class EntryExitFileHandler {

    private static final Path FILE = Path.of("data", "entry_exit.txt");

    public static List<String> read() {
        try {
            return Files.exists(FILE)
                    ? new ArrayList<>(Files.readAllLines(FILE))
                    : new ArrayList<>();

        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to read entry/exit records", e
            );
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
            throw new RuntimeException(
                    "Unable to save entry/exit record", e
            );
        }
    }

    public static void write(List<String> ls) {
        try {
            Files.createDirectories(FILE.getParent());

            Files.write(FILE, ls);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to update entry/exit records", e
            );
        }
    }
}
