
package lk.g41.vehicleparkingmanagementsystem.controller;

import lk.g41.vehicleparkingmanagementsystem.model.ParkingRecord;
import lk.g41.vehicleparkingmanagementsystem.service.EntryExitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EntryExitController {

    private final EntryExitService service;

    public EntryExitController(EntryExitService s) {
        service = s;
    }

    @GetMapping("/entry-exit")
    public String list(Model m) {
        m.addAttribute("records", service.all());
        return "entry-exit";
    }

    @GetMapping("/entry-exit/entry")
    public String entryForm() {
        return "vehicle-entry";
    }

    @PostMapping("/entry-exit/entry")
    public String entry(
            @RequestParam String vehicleNumber,
            Model m
    ) {
        if (service.entry(vehicleNumber)) {
            return "redirect:/entry-exit";
        }

        m.addAttribute(
                "error",
                "Invalid number, vehicle already parked, or no available parking slot."
        );
        m.addAttribute("vehicleNumber", vehicleNumber);

        return "vehicle-entry";
    }

    @GetMapping("/entry-exit/exit")
    public String exitForm(Model m) {
        List<ParkingRecord> activeRecords = service.all()
                .stream()
                .filter(r -> "Parked".equals(r.getStatus()))
                .toList();

        m.addAttribute("activeRecords", activeRecords);

        return "vehicle-exit";
    }

    @PostMapping("/entry-exit/exit")
    public String exit(
            @RequestParam String vehicleNumber,
            Model m
    ) {
        String searchedNumber = vehicleNumber.trim();

        ParkingRecord activeRecord = service.all()
                .stream()
                .filter(r -> "Parked".equals(r.getStatus()))
                .filter(r -> r.getVehicleNumber() != null
                        && r.getVehicleNumber().trim()
                        .equalsIgnoreCase(searchedNumber))
                .findFirst()
                .orElse(null);

        if (activeRecord != null
                && service.exit(activeRecord.getRecordId())) {
            return "redirect:/entry-exit";
        }

        m.addAttribute(
                "error",
                "No active parking session found for vehicle number: "
                        + searchedNumber
        );

        List<ParkingRecord> activeRecords = service.all()
                .stream()
                .filter(r -> "Parked".equals(r.getStatus()))
                .toList();

        m.addAttribute("activeRecords", activeRecords);
        m.addAttribute("vehicleNumber", vehicleNumber);

        return "vehicle-exit";
    }
}
