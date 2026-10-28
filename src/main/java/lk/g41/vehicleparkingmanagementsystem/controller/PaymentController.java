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
