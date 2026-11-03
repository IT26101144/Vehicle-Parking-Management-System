package lk.g41.vehicleparkingmanagementsystem.service;

import lk.g41.vehicleparkingmanagementsystem.model.ParkingSlot;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingMonitorService {

    // Get total number of parking slots
    public int getTotalSlots(List<ParkingSlot> slots) {
        return slots.size();
    }

    // Get number of occupied parking slots
    public int getOccupiedSlots(List<ParkingSlot> slots) {
        int count = 0;

        for (ParkingSlot slot : slots) {
            if (slot.isOccupied()) {
                count++;
            }
        }

        return count;
    }

    // Get number of available parking slots
    public int getAvailableSlots(List<ParkingSlot> slots) {
        return getTotalSlots(slots) - getOccupiedSlots(slots);
    }

    // Calculate parking occupancy rate as a percentage
    public double getOccupancyRate(List<ParkingSlot> slots) {
        if (slots.isEmpty()) {
            return 0.0;
        }

        return (getOccupiedSlots(slots) * 100.0) / getTotalSlots(slots);
    }
}