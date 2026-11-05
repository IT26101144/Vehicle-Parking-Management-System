package lk.g41.vehicleparkingmanagementsystem.service;

public class UserService {
    package lk.g41.vehicleparkingmanagementsystem.service;

import lk.g41.vehicleparkingmanagementsystem.model.User;
import lk.g41.vehicleparkingmanagementsystem.util.UserFileHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

    @Service
    public class UserService {

        private static final String DEFAULT_ROLE = "USER_MANAGEMENT";

        private static final List<String> VALID_ROLES = List.of(
                "ADMIN",
                "USER_MANAGEMENT",
                "VEHICLE_STAFF",
                "SECURITY",
                "PAYMENT"
        );

        // Convert a User object into a text-file record.
        private String toLine(User user) {
            return clean(user.getUserId()) + ","
                    + clean(user.getName()) + ","
                    + clean(user.getContactNumber()) + ","
                    + clean(user.getUsername()) + ","
                    + clean(user.getPassword()) + ","
                    + getSafeRole(user.getRole());
        }

        // Convert a text-file record into a User object.
        // Supports both old five-column and new six-column records.
        private User fromLine(String line) {
            if (line == null || line.isBlank()) {
                return null;
            }

            String[] parts = line.split(",", -1);

            if (parts.length != 5 && parts.length != 6) {
                return null;
            }

            String role = parts.length == 6
                    ? getSafeRole(parts[5])
                    : DEFAULT_ROLE;

            return new User(
                    parts[0],
                    parts[1],
                    parts[2],
                    parts[3],
                    parts[4],
                    role
            );
        }

        // READ: Get all users.
        public List<User> getAllUsers() {
            List<User> users = new ArrayList<>();

            for (String line : UserFileHandler.readUsers()) {
                User user = fromLine(line);

                if (user != null) {
                    users.add(user);
                }
            }

            return users;
        }

        // CREATE: Add a user with the default staff role.
        // Role assignment by an ordinary user is not allowed here.
        public boolean addUser(User user) {
            if (user == null
                    || isBlank(user.getUserId())
                    || isBlank(user.getName())
                    || isBlank(user.getContactNumber())
                    || isBlank(user.getUsername())
                    || isBlank(user.getPassword())) {
                return false;
            }

            List<String> lines = UserFileHandler.readUsers();

            for (String line : lines) {
                User existing = fromLine(line);

                if (existing == null) {
                    continue;
                }

                if (existing.getUserId().equalsIgnoreCase(
                        user.getUserId().trim())
                        || existing.getUsername().equalsIgnoreCase(
                        user.getUsername().trim())) {
                    return false;
                }
            }

            User newUser = new User(
                    user.getUserId().trim(),
                    user.getName().trim(),
                    user.getContactNumber().trim(),
                    user.getUsername().trim(),
                    user.getPassword(),
                    DEFAULT_ROLE
            );

            UserFileHandler.appendUser(toLine(newUser));
            return true;
        }

        // CREATE: Add a user with a specified role.
        // This method must only be called by an authorised admin controller.
        public boolean addUserWithRole(User user, String role) {
            if (user == null || isBlank(role)) {
                return false;
            }

            String normalisedRole = role.trim().toUpperCase();

            if (!VALID_ROLES.contains(normalisedRole)) {
                return false;
            }

            if (isBlank(user.getUserId())
                    || isBlank(user.getName())
                    || isBlank(user.getContactNumber())
                    || isBlank(user.getUsername())
                    || isBlank(user.getPassword())) {
                return false;
            }

            for (User existing : getAllUsers()) {
                if (existing.getUserId().equalsIgnoreCase(
                        user.getUserId().trim())
                        || existing.getUsername().equalsIgnoreCase(
                        user.getUsername().trim())) {
                    return false;
                }
            }

            User newUser = new User(
                    user.getUserId().trim(),
                    user.getName().trim(),
                    user.getContactNumber().trim(),
                    user.getUsername().trim(),
                    user.getPassword(),
                    normalisedRole
            );

            UserFileHandler.appendUser(toLine(newUser));
            return true;
        }

        // UPDATE: Update user details while preserving the existing role.
        public boolean updateUser(User updatedUser) {
            if (updatedUser == null
                    || isBlank(updatedUser.getUserId())
                    || isBlank(updatedUser.getName())
                    || isBlank(updatedUser.getContactNumber())
                    || isBlank(updatedUser.getUsername())
                    || isBlank(updatedUser.getPassword())) {
                return false;
            }

            List<String> lines = UserFileHandler.readUsers();
            boolean found = false;

            for (String line : lines) {
                User existing = fromLine(line);

                if (existing == null) {
                    continue;
                }

                if (existing.getUserId().equalsIgnoreCase(
                        updatedUser.getUserId().trim())) {
                    found = true;
                } else if (existing.getUsername().equalsIgnoreCase(
                        updatedUser.getUsername().trim())) {
                    return false;
                }
            }

            if (!found) {
                return false;
            }

            for (int i = 0; i < lines.size(); i++) {
                User existing = fromLine(lines.get(i));

                if (existing != null
                        && existing.getUserId().equalsIgnoreCase(
                        updatedUser.getUserId().trim())) {

                    User updated = new User(
                            updatedUser.getUserId().trim(),
                            updatedUser.getName().trim(),
                            updatedUser.getContactNumber().trim(),
                            updatedUser.getUsername().trim(),
                            updatedUser.getPassword(),
                            existing.getRole()
                    );

                    lines.set(i, toLine(updated));
                    UserFileHandler.writeUsers(lines);
                    return true;
                }
            }

            return false;
        }

        // DELETE: Remove a user by ID.
        public boolean deleteUser(String userId) {
            if (isBlank(userId)) {
                return false;
            }

            List<String> lines = UserFileHandler.readUsers();

            boolean removed = lines.removeIf(line -> {
                User user = fromLine(line);

                return user != null
                        && user.getUserId().equalsIgnoreCase(
                        userId.trim());
            });

            if (removed) {
                UserFileHandler.writeUsers(lines);
            }

            return removed;
        }

        // FIND: Find a user by ID.
        public User findUser(String userId) {
            if (isBlank(userId)) {
                return null;
            }

            for (User user : getAllUsers()) {
                if (user.getUserId().equalsIgnoreCase(userId.trim())) {
                    return user;
                }
            }

            return null;
        }

        // FIND: Find a user by username for login.
        public User findByUsername(String username) {
            if (isBlank(username)) {
                return null;
            }

            for (User user : getAllUsers()) {
                if (user.getUsername().equalsIgnoreCase(username.trim())) {
                    return user;
                }
            }

            return null;
        }

        private String getSafeRole(String role) {
            if (role == null || role.isBlank()) {
                return DEFAULT_ROLE;
            }

            String normalisedRole = role.trim().toUpperCase();

            return VALID_ROLES.contains(normalisedRole)
                    ? normalisedRole
                    : DEFAULT_ROLE;
        }

        private String clean(String value) {
            return value == null
                    ? ""
                    : value.trim().replace(",", " ");
        }

        private boolean isBlank(String value) {
            return value == null || value.trim().isEmpty();
        }
    }
}
