package com.hms.dto;

import jakarta.validation.constraints.NotNull;

public record BillingRequest(
    @NotNull Long appointmentId
) {}