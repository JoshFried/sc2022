package com.jf.sc2022.dto.payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequestDTO {
    private double amount;
    private String currency;
    private String email;
    private long   userId;
    private long   imageListingId;
}
