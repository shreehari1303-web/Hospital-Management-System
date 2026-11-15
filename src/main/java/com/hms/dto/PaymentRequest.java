package com.hms.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import com.hms.entity.PaymentMethod;

public record PaymentRequest(

    @NotNull
    Long billId,

    @NotNull
    @DecimalMin(value = "0.01")
    BigDecimal amount,

    @NotNull
    PaymentMethod paymentMethod

) {}