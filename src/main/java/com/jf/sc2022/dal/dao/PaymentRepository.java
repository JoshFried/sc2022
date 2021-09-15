package com.jf.sc2022.dal.dao;

import com.jf.sc2022.dal.model.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Long> {
}
