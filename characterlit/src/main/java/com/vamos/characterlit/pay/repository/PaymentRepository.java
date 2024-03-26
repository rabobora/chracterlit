package com.vamos.characterlit.pay.repository;

import com.vamos.characterlit.pay.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    Payment findByPaymentId(String PaymentID);
}
