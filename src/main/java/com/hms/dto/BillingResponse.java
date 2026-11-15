package com.hms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.hms.entity.PaymentStatus;

public record BillingResponse(
    Long billId,
    Long appointmentId,
    String patientName,
    String doctorName,
    BigDecimal amount,
    LocalDate billDate,
    PaymentStatus paymentStatus
) {}