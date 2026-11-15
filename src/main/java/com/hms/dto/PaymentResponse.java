package com.hms.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.hms.entity.PaymentMethod;
import com.hms.entity.PaymentStatus;

public record PaymentResponse(
    Long paymentId,
    Long billId,
    BigDecimal amount,
    PaymentMethod paymentMethod,
    LocalDateTime paymentDate,
    PaymentStatus paymentStatus
) {}