package com.hms.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hms.dto.BillingRequest;
import com.hms.dto.BillingResponse;
import com.hms.entity.Appointment;
import com.hms.entity.Billing;
import com.hms.entity.PaymentStatus;
import com.hms.repository.AppointmentRepository;
import com.hms.repository.BillingRepository;

@Service
@Transactional
public class BillingService {

    private final BillingRepository billingRepository;
    private final AppointmentRepository appointmentRepository;

    public BillingService(
            BillingRepository billingRepository,
            AppointmentRepository appointmentRepository) {
        this.billingRepository = billingRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public BillingResponse generateBill(BillingRequest request) {

        Appointment appointment = appointmentRepository
                .findById(request.appointmentId())
                .orElseThrow(() ->
                    new RuntimeException("Appointment not found"));

        if (billingRepository.existsByAppointmentAppointmentId(
                request.appointmentId())) {
            throw new RuntimeException(
                "Bill already exists for this appointment");
        }

        BigDecimal amount = BigDecimal.valueOf(
                appointment.getDoctor().getConsultationFee());

        Billing billing = new Billing();
        billing.setAppointment(appointment);
        billing.setAmount(amount);
        billing.setBillDate(LocalDate.now());
        billing.setPaymentStatus(PaymentStatus.PENDING);

        Billing savedBill = billingRepository.save(billing);

        return convertToResponse(savedBill);
    }

    @Transactional(readOnly = true)
    public List<BillingResponse> getAllBills() {

        return billingRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BillingResponse getBillById(Long id) {

        Billing billing = billingRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Bill not found"));

        return convertToResponse(billing);
    }

    private BillingResponse convertToResponse(Billing billing) {

        Appointment appointment = billing.getAppointment();

        return new BillingResponse(
                billing.getBillId(),
                appointment.getAppointmentId(),
                appointment.getPatient().getFullName(),
                appointment.getDoctor().getFullName(),
                billing.getAmount(),
                billing.getBillDate(),
                billing.getPaymentStatus()
        );
    }
}