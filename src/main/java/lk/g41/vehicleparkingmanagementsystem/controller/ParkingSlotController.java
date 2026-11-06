package lk.g41.vehicleparkingmanagementsystem.controller;

import lk.g41.vehicleparkingmanagementsystem.model.ParkingSlot;
import lk.g41.vehicleparkingmanagementsystem.service.ParkingSlotService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ParkingSlotController {

    private final ParkingSlotService service;

    public ParkingSlotController(ParkingSlotService service) {
        this.service = service;
    }

    @GetMapping("/parking-slots")
    public String list(Model model) {
        populatePage(model);
        return "parking-slots";
    }

    @GetMapping("/parking-slots/add")
    public String addForm(Model model) {
        model.addAttribute("slot", new ParkingSlot());
        return "add-parking-slot";
    }

    @PostMapping("/parking-slots/add")
    public String add(ParkingSlot slot, Model model) {
        if (service.add(slot)) {
            return "redirect:/parking-slots";
        }

        model.addAttribute("slot", slot);
        model.addAttribute(
                "error",
                "Slot ID already exists or required fields are missing."
        );

        return "add-parking-slot";
    }

    @GetMapping("/parking-slots/edit")
    public String editForm(
            @RequestParam String slotId,
            Model model) {

        ParkingSlot slot = service.find(slotId);

        if (slot == null) {
            return "redirect:/parking-slots";
        }

        model.addAttribute("slot", slot);
        return "edit-parking-slot";
    }

    @PostMapping("/parking-slots/edit")
    public String edit(ParkingSlot slot, Model model) {
        if (service.update(slot)) {
            return "redirect:/parking-slots";
        }

        model.addAttribute("slot", slot);
        model.addAttribute("error", "Unable to update slot.");

        return "edit-parking-slot";
    }

    @PostMapping("/parking-slots/delete")
    public String delete(@RequestParam String slotId) {
        service.delete(slotId);
        return "redirect:/parking-slots";
    }

    @PostMapping("/parking-slots/allocate")
    public String allocate(
            @RequestParam String slotId,
            @RequestParam String vehicleNumber,
            Model model) {

        boolean success = service.allocate(slotId, vehicleNumber);

        model.addAttribute(
                "message",
                success
                        ? "Slot allocated successfully."
                        : "Allocation failed. Register the vehicle first, "
                        + "check the slot availability, and ensure the "
                        + "vehicle is not already parked."
        );

        populatePage(model);
        return "parking-slots";
    }

    @PostMapping("/parking-slots/release")
    public String release(@RequestParam String slotId) {
        service.release(slotId);
        return "redirect:/parking-slots";
    }

    private void populatePage(Model model) {
        model.addAttribute("slots", service.getAll());
        model.addAttribute("available", service.availableCount());
        model.addAttribute("occupied", service.occupiedCount());
    }
}

//Dilshan update 104