package lk.g41.vehicleparkingmanagementsystem.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class UserFileHandler {

    private static final String USER_FILE = "data/users.txt";

    public static void appendUser(String data) {
        try {
            Path path = Paths.get(USER_FILE);
            Files.createDirectories(path.getParent());

            Files.writeString(
                    path,
                    data + System.lineSeparator(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new RuntimeException("Unable to save user data.", e);
        }
    }

    public static List<String> readUsers() {
        try {
            Path path = Paths.get(USER_FILE);

            if (!Files.exists(path)) {
                return List.of();
            }

            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read user data.", e);
        }
    }

    public static void writeUsers(List<String> users) {
        try {
            Path path = Paths.get(USER_FILE);
            Files.createDirectories(path.getParent());
            Files.write(path, users);
        } catch (IOException e) {
            throw new RuntimeException("Unable to update user data.", e);
        }
    }
}