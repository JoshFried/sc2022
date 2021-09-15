package com.jf.sc2022.converter;


import com.jf.sc2022.dal.model.Payment;
import com.jf.sc2022.dto.PaymentDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class PaymentToPaymentDTOConverter implements Converter<Payment, PaymentDTO> {
    @Override
    public PaymentDTO convert(final Payment payment) {
        return PaymentDTO.builder()
                         .amount(payment.getAmount())
                         .chargeId(payment.getChargeId())
                         .email(payment.getEmail())
                         .id(payment.getId())
                         .build();
    }
}
