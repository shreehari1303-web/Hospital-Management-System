package com.hms.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hms.entity.Doctor;
import com.hms.repository.DoctorRepository;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // CREATE
    public Doctor addDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // READ ALL
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // READ BY ID
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Doctor not found with ID: " + id
                )
            );
    }

    // UPDATE
    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {

        Doctor existingDoctor = getDoctorById(id);

        existingDoctor.setFullName(updatedDoctor.getFullName());
        existingDoctor.setSpecialization(
            updatedDoctor.getSpecialization()
        );
        existingDoctor.setPhone(updatedDoctor.getPhone());
        existingDoctor.setEmail(updatedDoctor.getEmail());
        existingDoctor.setConsultationFee(
            updatedDoctor.getConsultationFee()
        );
        existingDoctor.setAvailable(updatedDoctor.isAvailable());

        return doctorRepository.save(existingDoctor);
    }

    // DELETE
    public void deleteDoctor(Long id) {

        Doctor existingDoctor = getDoctorById(id);

        doctorRepository.delete(existingDoctor);
    }
}