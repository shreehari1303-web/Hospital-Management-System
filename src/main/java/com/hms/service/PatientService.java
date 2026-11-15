package com.hms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hms.entity.Patient;
import com.hms.repository.PatientRepository;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {

        return patientRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Patient not found: " + id)
                );
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {

        Patient patient = getPatientById(id);

        patient.setFullName(updatedPatient.getFullName());
        patient.setGender(updatedPatient.getGender());
        patient.setDateOfBirth(updatedPatient.getDateOfBirth());
        patient.setPhone(updatedPatient.getPhone());
        patient.setEmail(updatedPatient.getEmail());
        patient.setAddress(updatedPatient.getAddress());

        return patientRepository.save(patient);
    }

    public void deletePatient(Long id) {

        Patient patient = getPatientById(id);

        patientRepository.delete(patient);
    }
}