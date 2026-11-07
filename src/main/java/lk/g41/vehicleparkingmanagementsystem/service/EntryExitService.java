
package lk.g41.vehicleparkingmanagementsystem.service;

import lk.g41.vehicleparkingmanagementsystem.model.ParkingRecord;
import lk.g41.vehicleparkingmanagementsystem.util.EntryExitFileHandler;
import lk.g41.vehicleparkingmanagementsystem.model.ParkingSlot;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class EntryExitService {

    private final ParkingSlotService slotService;

    public EntryExitService(ParkingSlotService slotService) {
        this.slotService = slotService;
    }

    private static final DateTimeFormatter F =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String line(ParkingRecord r) {
        return clean(r.getRecordId()) + ","
                + clean(r.getVehicleNumber()) + ","
                + clean(r.getEntryTime()) + ","
                + clean(r.getExitTime()) + ","
                + clean(r.getStatus());
    }

    private String clean(String s) {
        return s == null ? "" : s.trim().replace(",", " ");
    }

    private ParkingRecord parse(String l) {
        String[] p = l.split(",", -1);

        return p.length == 5
                ? new ParkingRecord(p[0], p[1], p[2], p[3], p[4])
                : null;
    }

    public List<ParkingRecord> all() {
        List<ParkingRecord> a = new ArrayList<>();

        for (String l : EntryExitFileHandler.read()) {
            ParkingRecord r = parse(l);

            if (r != null) {
                a.add(r);
            }
        }

        return a;
    }

    public boolean entry(String number) {
        if (number == null || number.trim().isEmpty()) {
            return false;
        }

        for (ParkingRecord r : all()) {
            if (r.getVehicleNumber().equalsIgnoreCase(number.trim())
                    && "Parked".equalsIgnoreCase(r.getStatus())) {
                return false;
            }
        }

        ParkingSlot available = slotService.getAll()
                .stream()
                .filter(s -> "Available".equalsIgnoreCase(s.getStatus()))
                .findFirst()
                .orElse(null);

        if (available == null) {
            return false;
        }

        if (!slotService.allocate(available.getSlotId(), number.trim())) {
            return false;
        }

        EntryExitFileHandler.append(
                line(
                        new ParkingRecord(
                                "REC" + System.currentTimeMillis(),
                                number.trim(),
                                LocalDateTime.now().format(F),
                                "",
                                "Parked"
                        )
                )
        );

        return true;
    }

    public boolean exit(String id) {
        List<String> ls = EntryExitFileHandler.read();

        for (int i = 0; i < ls.size(); i++) {
            ParkingRecord r = parse(ls.get(i));

            if (r != null
                    && r.getRecordId().equals(id)
                    && "Parked".equalsIgnoreCase(r.getStatus())) {

                r.setExitTime(LocalDateTime.now().format(F));
                r.setStatus("Exited");

                ls.set(i, line(r));

                EntryExitFileHandler.write(ls);

                for (ParkingSlot slot : slotService.getAll()) {
                    if (slot.isOccupied()
                            && slot.getVehicleNumber()
                            .equalsIgnoreCase(r.getVehicleNumber())) {

                        slotService.release(slot.getSlotId());
                        break;
                    }
                }

                return true;
            }
        }

        return false;
    }

    public ParkingRecord find(String id) {
        return all()
                .stream()
                .filter(r -> r.getRecordId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public double durationHours(ParkingRecord r) {
        try {
            LocalDateTime a = LocalDateTime.parse(r.getEntryTime(), F);

            LocalDateTime b =
                    (r.getExitTime() == null || r.getExitTime().isBlank())
                            ? LocalDateTime.now()
                            : LocalDateTime.parse(r.getExitTime(), F);

            return Math.max(
                    0,
                    Duration.between(a, b).toMinutes() / 60.0
            );

        } catch (Exception e) {
            return 0;
        }
    }
}
