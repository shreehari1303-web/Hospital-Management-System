package com.hms.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hms.dto.PaymentRequest;
import com.hms.dto.PaymentResponse;
import com.hms.entity.Billing;
import com.hms.entity.Payment;
import com.hms.entity.PaymentStatus;
import com.hms.repository.BillingRepository;
import com.hms.repository.PaymentRepository;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillingRepository billingRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            BillingRepository billingRepository) {

        this.paymentRepository = paymentRepository;
        this.billingRepository = billingRepository;
    }

    public PaymentResponse makePayment(PaymentRequest request) {

        Billing billing = billingRepository
                .findById(request.billId())
                .orElseThrow(() ->
                    new RuntimeException("Bill not found"));

        if (billing.getPaymentStatus() == PaymentStatus.PAID) {
            throw new RuntimeException("Bill is already paid");
        }

        // Calculate previous successful payments
        List<Payment> payments =
                paymentRepository.findByBillingBillId(
                        request.billId());

        BigDecimal totalPaid = payments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.PAID)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remainingAmount =
                billing.getAmount().subtract(totalPaid);

        if (request.amount().compareTo(remainingAmount) > 0) {
            throw new RuntimeException(
                    "Payment exceeds remaining bill amount: "
                    + remainingAmount);
        }

        // Create payment record
        Payment payment = new Payment();

        payment.setBilling(billing);
        payment.setAmount(request.amount());
        payment.setPaymentMethod(request.paymentMethod());
        payment.setPaymentDate(LocalDateTime.now());

        // Simulated successful payment
        payment.setPaymentStatus(PaymentStatus.PAID);

        Payment savedPayment =
                paymentRepository.save(payment);

        // Calculate updated total
        BigDecimal updatedTotal =
                totalPaid.add(request.amount());

        // Update bill status
        if (updatedTotal.compareTo(billing.getAmount()) == 0) {
            billing.setPaymentStatus(PaymentStatus.PAID);
        } else {
            billing.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);
        }

        billingRepository.save(billing);

        return convertToResponse(savedPayment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByBillId(Long billId) {

        if (!billingRepository.existsById(billId)) {
            throw new RuntimeException("Bill not found");
        }

        return paymentRepository
                .findByBillingBillId(billId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private PaymentResponse convertToResponse(Payment payment) {

        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getBilling().getBillId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentDate(),
                payment.getPaymentStatus()
        );
    }
}