package lk.g41.vehicleparkingmanagementsystem.controller;

import lk.g41.vehicleparkingmanagementsystem.model.Vehicle;
import lk.g41.vehicleparkingmanagementsystem.service.VehicleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VehicleController {
    private final VehicleService vehicleService;
    public VehicleController(VehicleService vehicleService) { this.vehicleService = vehicleService; }

    @GetMapping("/vehicles")
    public String showVehicles(Model model) {
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        return "vehicles";
    }

    @GetMapping("/vehicles/add")
    public String showAddVehicleForm(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        return "add-vehicle";
    }

    @PostMapping("/vehicles/add")
    public String addVehicle(Vehicle vehicle, Model model) {
        if (!vehicleService.registerVehicle(vehicle)) {
            model.addAttribute("error", "Vehicle ID or vehicle number already exists, or details are invalid.");
            model.addAttribute("vehicle", vehicle);
            return "add-vehicle";
        }
        return "redirect:/vehicles";
    }

    @GetMapping("/vehicles/edit")
    public String showEditVehicleForm(@RequestParam String vehicleId, Model model) {
        Vehicle vehicle = vehicleService.findVehicle(vehicleId);
        if (vehicle == null) return "redirect:/vehicles";
        model.addAttribute("vehicle", vehicle);
        return "edit-vehicle";
    }

    @PostMapping("/vehicles/edit")
    public String updateVehicle(Vehicle vehicle, Model model) {
        if (!vehicleService.updateVehicle(vehicle.getVehicleId(), vehicle.getVehicleNumber(), vehicle.getVehicleType(), vehicle.getOwnerName())) {
            model.addAttribute("error", "Could not update vehicle. Check duplicate vehicle number and required fields.");
            model.addAttribute("vehicle", vehicle);
            return "edit-vehicle";
        }
        return "redirect:/vehicles";
    }

    @PostMapping("/vehicles/delete")
    public String deleteVehicle(@RequestParam String vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return "redirect:/vehicles";
    }
}
