package com.hms.dto;

import java.time.LocalDateTime;

import com.hms.entity.AppointmentStatus;

public record AppointmentResponse(

    Long appointmentId,
    Long patientId,
    String patientName,
    Long doctorId,
    String doctorName,
    LocalDateTime appointmentDateTime,
    String reason,
    AppointmentStatus status

) {
}