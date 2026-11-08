package lk.g41.vehicleparkingmanagementsystem.controller;

import lk.g41.vehicleparkingmanagementsystem.model.Payment;
import lk.g41.vehicleparkingmanagementsystem.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @GetMapping("/payments")
    public String list(Model model) {
        model.addAttribute("payments", service.all());
        model.addAttribute("totalPayments", service.total());
        return "payments";
    }

    @GetMapping("/payments/add")
    public String form(Model model) {
        model.addAttribute("payment", new Payment());
        model.addAttribute(
                "unpaidRecords", service.getUnpaidExitedRecords());
        return "add-payment";
    }

    @PostMapping("/payments/add")
    public String add(
            @RequestParam String recordId,
            @RequestParam String paymentMethod,
            Model model) {

        if (service.add(recordId, paymentMethod)) {
            return "redirect:/payments";
        }

        model.addAttribute("payment", new Payment());
        model.addAttribute(
                "unpaidRecords", service.getUnpaidExitedRecords());
        model.addAttribute(
                "error",
                "Payment could not be saved. Check the parking session.");
        return "add-payment";
    }

    @GetMapping("/payments/details")
    public String details(
            @RequestParam String paymentId, Model model) {

        Payment payment = service.find(paymentId);

        if (payment == null) {
            return "redirect:/payments";
        }

        model.addAttribute("payment", payment);
        return "payment-details";
    }

    @PostMapping("/payments/delete")
    public String delete(@RequestParam String paymentId) {
        service.delete(paymentId);
        return "redirect:/payments";
    }
}

