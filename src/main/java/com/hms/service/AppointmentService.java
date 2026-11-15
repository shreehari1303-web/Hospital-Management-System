package com.hms.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hms.dto.AppointmentRequest;
import com.hms.dto.AppointmentResponse;
import com.hms.entity.*;
import com.hms.repository.*;

@Service
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {

        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    // CREATE APPOINTMENT
    public AppointmentResponse createAppointment(
            AppointmentRequest request) {

        Patient patient = patientRepository
            .findById(request.patientId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Patient not found"));

        Doctor doctor = doctorRepository
            .findById(request.doctorId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Doctor not found"));

        if (!doctor.isAvailable()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Doctor is currently unavailable");
        }

        Appointment appointment = new Appointment();

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDateTime(
            request.appointmentDateTime());
        appointment.setReason(request.reason());
        appointment.setStatus(AppointmentStatus.PENDING);

        Appointment saved =
            appointmentRepository.save(appointment);

        return convertToResponse(saved);
    }

    // GET ALL APPOINTMENTS
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointments() {

        return appointmentRepository.findAll()
            .stream()
            .map(this::convertToResponse)
            .toList();
    }

    // GET APPOINTMENT BY ID
    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long id) {

        Appointment appointment = findAppointment(id);

        return convertToResponse(appointment);
    }

    // UPDATE APPOINTMENT
    public AppointmentResponse updateAppointment(
            Long id, AppointmentRequest request) {

        Appointment appointment = findAppointment(id);

        Patient patient = patientRepository
            .findById(request.patientId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Patient not found"));

        Doctor doctor = doctorRepository
            .findById(request.doctorId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Doctor not found"));

        if (!doctor.isAvailable()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Doctor is currently unavailable");
        }

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDateTime(
            request.appointmentDateTime());
        appointment.setReason(request.reason());

        if (request.status() != null) {
            appointment.setStatus(request.status());
        }

        Appointment updated =
            appointmentRepository.save(appointment);

        return convertToResponse(updated);
    }

    // DELETE APPOINTMENT
    public void deleteAppointment(Long id) {

        Appointment appointment = findAppointment(id);

        appointmentRepository.delete(appointment);
    }

    // FIND APPOINTMENT
    private Appointment findAppointment(Long id) {

        return appointmentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Appointment not found with ID: " + id));
    }

    // CONVERT ENTITY INTO RESPONSE DTO
    private AppointmentResponse convertToResponse(
            Appointment appointment) {

        return new AppointmentResponse(
            appointment.getAppointmentId(),
            appointment.getPatient().getPatientId(),
            appointment.getPatient().getFullName(),
            appointment.getDoctor().getDoctorId(),
            appointment.getDoctor().getFullName(),
            appointment.getAppointmentDateTime(),
            appointment.getReason(),
            appointment.getStatus()
        );
    }
}