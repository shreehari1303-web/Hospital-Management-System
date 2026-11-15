package com.hms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hms.entity.Payment;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    List<Payment> findByBillingBillId(Long billId);
}