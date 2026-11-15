package com.hms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.hms.entity.Doctor;
import com.hms.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:5173")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // CREATE DOCTOR
    @PostMapping
    public ResponseEntity<Doctor> addDoctor(
            @Valid @RequestBody Doctor doctor) {

        Doctor savedDoctor = doctorService.addDoctor(doctor);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedDoctor);
    }

    // GET ALL DOCTORS
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {

        return ResponseEntity.ok(
            doctorService.getAllDoctors()
        );
    }

    // GET DOCTOR BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
            doctorService.getDoctorById(id)
        );
    }

    // UPDATE DOCTOR
    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody Doctor doctor) {

        Doctor updatedDoctor =
            doctorService.updateDoctor(id, doctor);

        return ResponseEntity.ok(updatedDoctor);
    }

    // DELETE DOCTOR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(
            @PathVariable Long id) {

        doctorService.deleteDoctor(id);

        return ResponseEntity.noContent().build();
    }
}