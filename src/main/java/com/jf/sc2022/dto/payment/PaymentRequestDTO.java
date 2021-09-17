package com.jf.sc2022.dto.payment;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private long   amount;
    private String currency;
    private String email;
    private long   userId;
    private long   imageListingId;
}
