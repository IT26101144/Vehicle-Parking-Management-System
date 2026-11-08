
package lk.g41.vehicleparkingmanagementsystem.controller;

import lk.g41.vehicleparkingmanagementsystem.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final VehicleService vehicles;
    private final ParkingSlotService slots;
    private final EntryExitService entries;
    private final PaymentService payments;

    public DashboardController(
            VehicleService v,
            ParkingSlotService s,
            EntryExitService e,
            PaymentService p
    ) {
        vehicles = v;
        slots = s;
        entries = e;
        payments = p;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model m) {

        m.addAttribute(
                "vehicleCount",
                vehicles.getAllVehicles().size()
        );

        m.addAttribute(
                "slotCount",
                slots.getAll().size()
        );

        m.addAttribute(
                "availableCount",
                slots.availableCount()
        );

        m.addAttribute(
                "occupiedCount",
                slots.occupiedCount()
        );

        m.addAttribute(
                "parkedCount",
                entries.all()
                        .stream()
                        .filter(r -> "Parked".equalsIgnoreCase(r.getStatus()))
                        .count()
        );

        m.addAttribute(
                "paymentTotal",
                payments.total()
        );

        m.addAttribute(
                "recentRecords",
                entries.all()
                        .stream()
                        .sorted((a, b) ->
                                b.getEntryTime().compareTo(a.getEntryTime()))
                        .limit(6)
                        .toList()
        );

        return "dashboard";
    }
}
