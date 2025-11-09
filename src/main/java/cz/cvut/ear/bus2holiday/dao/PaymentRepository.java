package cz.cvut.ear.bus2holiday.dao;

import cz.cvut.ear.bus2holiday.model.Payment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {}
