
package lk.g41.vehicleparkingmanagementsystem.controller;

import lk.g41.vehicleparkingmanagementsystem.model.ParkingReport;
import lk.g41.vehicleparkingmanagementsystem.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService s) {
        service = s;
    }

    @GetMapping("/parking-reports")
    public String list(
            @RequestParam(defaultValue = "vehicle") String sortBy,
            Model m
    ) {
        List<ParkingReport> a = service.all();

        if ("duration".equalsIgnoreCase(sortBy)) {
            service.sortDuration(a);
        } else {
            service.sortVehicle(a);
        }

        m.addAttribute("reports", a);
        m.addAttribute("sortBy", sortBy);
        m.addAttribute("parked", service.parked());
        m.addAttribute("exited", service.exited());
        m.addAttribute("total", a.size());

        return "parking-reports";
    }

    @GetMapping("/parking-reports/details")
    public String details(
            @RequestParam String vehicleNumber,
            Model m
    ) {
        for (ParkingReport r : service.all()) {
            if (r.getVehicleNumber().equalsIgnoreCase(vehicleNumber)) {
                m.addAttribute("report", r);

                return "report-details";
            }
        }

        return "redirect:/parking-reports";
    }
}
