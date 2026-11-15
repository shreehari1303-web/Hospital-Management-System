package com.hms.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.hms.entity.AppointmentStatus;

public record AppointmentRequest(

    @NotNull
    Long patientId,

    @NotNull
    Long doctorId,

    @NotNull
    LocalDateTime appointmentDateTime,

    @NotBlank
    String reason,

    AppointmentStatus status

) {
}