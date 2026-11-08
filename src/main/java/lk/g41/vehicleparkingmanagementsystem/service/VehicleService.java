
package lk.g41.vehicleparkingmanagementsystem.service;

import lk.g41.vehicleparkingmanagementsystem.model.Vehicle;
import lk.g41.vehicleparkingmanagementsystem.util.FileHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleService {

    private String toLine(Vehicle v) {
        return clean(v.getVehicleId()) + ","
                + clean(v.getVehicleNumber()) + ","
                + clean(v.getVehicleType()) + ","
                + clean(v.getOwnerName());
    }

    private String clean(String s) {
        return s == null ? "" : s.trim().replace(",", " ");
    }

    public boolean registerVehicle(Vehicle v) {
        if (v == null
                || blank(v.getVehicleId())
                || blank(v.getVehicleNumber())
                || blank(v.getVehicleType())
                || blank(v.getOwnerName())) {
            return false;
        }

        for (Vehicle existing : getAllVehicles()) {
            if (existing.getVehicleId().equalsIgnoreCase(v.getVehicleId())
                    || existing.getVehicleNumber()
                    .equalsIgnoreCase(v.getVehicleNumber())) {
                return false;
            }
        }

        FileHandler.appendVehicle(toLine(v));
        return true;
    }

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();

        for (String line : FileHandler.readVehicles()) {
            String[] p = line.split(",", -1);

            if (p.length == 4) {
                vehicles.add(new Vehicle(p[0], p[1], p[2], p[3]));
            }
        }

        return vehicles;
    }

    public Vehicle findVehicle(String id) {
        return getAllVehicles()
                .stream()
                .filter(v -> v.getVehicleId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public boolean updateVehicle(
            String id,
            String number,
            String type,
            String owner
    ) {
        if (blank(id) || blank(number) || blank(type) || blank(owner)) {
            return false;
        }

        List<String> lines = FileHandler.readVehicles();
        boolean found = false;

        for (int i = 0; i < lines.size(); i++) {
            String[] p = lines.get(i).split(",", -1);

            if (p.length == 4) {
                if (p[0].equalsIgnoreCase(id)) {
                    found = true;
                    continue;
                }

                if (p[1].equalsIgnoreCase(number)) {
                    return false;
                }
            }
        }

        if (!found) {
            return false;
        }

        for (int i = 0; i < lines.size(); i++) {
            String[] p = lines.get(i).split(",", -1);

            if (p.length == 4 && p[0].equalsIgnoreCase(id)) {
                lines.set(
                        i,
                        id + ","
                                + clean(number) + ","
                                + clean(type) + ","
                                + clean(owner)
                );
                break;
            }
        }

        FileHandler.writeVehicles(lines);
        return true;
    }

    public boolean deleteVehicle(String id) {
        List<String> lines = FileHandler.readVehicles();

        boolean removed = lines.removeIf(line -> {
            String[] p = line.split(",", -1);
            return p.length == 4
                    && p[0].equalsIgnoreCase(id);
        });

        if (removed) {
            FileHandler.writeVehicles(lines);
        }

        return removed;
    }

    private boolean blank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
