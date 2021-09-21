package com.jf.sc2022.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class PaymentDTO {
    long   id;
    double amount;
    String email;
    String chargeId;
}
