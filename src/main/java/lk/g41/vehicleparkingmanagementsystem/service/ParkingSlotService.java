
package lk.g41.vehicleparkingmanagementsystem.service;

import lk.g41.vehicleparkingmanagementsystem.model.ParkingSlot;
import lk.g41.vehicleparkingmanagementsystem.model.Vehicle;
import lk.g41.vehicleparkingmanagementsystem.util.ParkingSlotFileHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingSlotService {

    private final VehicleService vehicleService;

    public ParkingSlotService(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    private String line(ParkingSlot slot) {
        return clean(slot.getSlotId()) + ","
                + clean(slot.getSlotType()) + ","
                + clean(slot.getStatus()) + ","
                + clean(slot.getVehicleNumber());
    }

    private String clean(String value) {
        return value == null ? "" : value.trim().replace(",", " ");
    }

    private boolean blank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private ParkingSlot parse(String line) {
        if (line == null) {
            return null;
        }

        String[] parts = line.split(",", -1);

        return parts.length == 4
                ? new ParkingSlot(
                parts[0], parts[1], parts[2], parts[3])
                : null;
    }

    public List<ParkingSlot> getAll() {
        List<ParkingSlot> slots = new ArrayList<>();

        for (String record : ParkingSlotFileHandler.read()) {
            ParkingSlot slot = parse(record);

            if (slot != null) {
                slots.add(slot);
            }
        }

        return slots;
    }

    public ParkingSlot find(String id) {
        if (blank(id)) {
            return null;
        }

        return getAll().stream()
                .filter(slot ->
                        slot.getSlotId().equalsIgnoreCase(id.trim()))
                .findFirst()
                .orElse(null);
    }

    public boolean add(ParkingSlot slot) {
        if (slot == null
                || blank(slot.getSlotId())
                || blank(slot.getSlotType())
                || find(slot.getSlotId()) != null) {
            return false;
        }

        slot.setStatus("Available");
        slot.setVehicleNumber("");

        ParkingSlotFileHandler.append(line(slot));
        return true;
    }

    public boolean update(ParkingSlot slot) {
        if (slot == null
                || blank(slot.getSlotId())
                || blank(slot.getSlotType())) {
            return false;
        }

        List<String> records = ParkingSlotFileHandler.read();

        for (int i = 0; i < records.size(); i++) {
            ParkingSlot existing = parse(records.get(i));

            if (existing != null
                    && existing.getSlotId().equalsIgnoreCase(
                    slot.getSlotId().trim())) {

                // Preserve allocation information during an edit.
                slot.setStatus(existing.getStatus());
                slot.setVehicleNumber(existing.getVehicleNumber());

                records.set(i, line(slot));
                ParkingSlotFileHandler.write(records);
                return true;
            }
        }

        return false;
    }

    public boolean delete(String id) {
        if (blank(id)) {
            return false;
        }

        List<String> records = ParkingSlotFileHandler.read();

        for (int i = 0; i < records.size(); i++) {
            ParkingSlot slot = parse(records.get(i));

            if (slot != null
                    && slot.getSlotId().equalsIgnoreCase(id.trim())) {

                if (slot.isOccupied()) {
                    return false;
                }

                records.remove(i);
                ParkingSlotFileHandler.write(records);
                return true;
            }
        }

        return false;
    }

    // Check whether the vehicle is already assigned to an occupied slot.
    private boolean isVehicleAlreadyParked(String vehicleNumber) {
        for (ParkingSlot slot : getAll()) {
            if (slot.isOccupied()
                    && !blank(slot.getVehicleNumber())
                    && slot.getVehicleNumber().trim()
                    .equalsIgnoreCase(vehicleNumber.trim())) {
                return true;
            }
        }

        return false;
    }

    // Only registered vehicles can be allocated.
    private boolean isRegisteredVehicle(String vehicleNumber) {
        for (Vehicle vehicle : vehicleService.getAllVehicles()) {
            if (vehicle.getVehicleNumber() != null
                    && vehicle.getVehicleNumber().trim()
                    .equalsIgnoreCase(vehicleNumber.trim())) {
                return true;
            }
        }

        return false;
    }

    public boolean allocate(String id, String vehicleNumber) {
        if (blank(id) || blank(vehicleNumber)) {
            return false;
        }

        String normalisedNumber = vehicleNumber.trim();

        // Reject vehicle numbers absent from the vehicle directory.
        if (!isRegisteredVehicle(normalisedNumber)) {
            return false;
        }

        // One vehicle can occupy only one slot at a time.
        if (isVehicleAlreadyParked(normalisedNumber)) {
            return false;
        }

        List<String> records = ParkingSlotFileHandler.read();

        for (int i = 0; i < records.size(); i++) {
            ParkingSlot slot = parse(records.get(i));

            if (slot != null
                    && slot.getSlotId().equalsIgnoreCase(id.trim())
                    && "Available".equalsIgnoreCase(slot.getStatus())) {

                slot.setStatus("Occupied");
                slot.setVehicleNumber(normalisedNumber);

                records.set(i, line(slot));
                ParkingSlotFileHandler.write(records);

                return true;
            }
        }

        return false;
    }

    public boolean release(String id) {
        if (blank(id)) {
            return false;
        }

        List<String> records = ParkingSlotFileHandler.read();

        for (int i = 0; i < records.size(); i++) {
            ParkingSlot slot = parse(records.get(i));

            if (slot != null
                    && slot.getSlotId().equalsIgnoreCase(id.trim())
                    && slot.isOccupied()) {

                slot.setStatus("Available");
                slot.setVehicleNumber("");

                records.set(i, line(slot));
                ParkingSlotFileHandler.write(records);

                return true;
            }
        }

        return false;
    }

    public long availableCount() {
        return getAll().stream()
                .filter(slot ->
                        "Available".equalsIgnoreCase(slot.getStatus()))
                .count();
    }

    public long occupiedCount() {
        return getAll().stream()
                .filter(ParkingSlot::isOccupied)
                .count();
    }
}
