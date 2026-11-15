package com.hms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.hms.dto.BillingRequest;
import com.hms.dto.BillingResponse;
import com.hms.service.BillingService;

@RestController
@RequestMapping("/api/billings")
@CrossOrigin(origins = "http://localhost:5173")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping
    public ResponseEntity<BillingResponse> generateBill(
            @Valid @RequestBody BillingRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(billingService.generateBill(request));
    }

    @GetMapping
    public ResponseEntity<List<BillingResponse>> getAllBills() {
        return ResponseEntity.ok(billingService.getAllBills());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillingResponse> getBillById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                billingService.getBillById(id));
    }
}