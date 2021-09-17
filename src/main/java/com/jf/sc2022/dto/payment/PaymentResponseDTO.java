package com.jf.sc2022.dto.payment;

import lombok.Builder;
import lombok.Getter;

@Builder

public class PaymentResponseDTO {
    private         String clientSecret;
    @Getter private String clientName;
    @Getter private String listingTitle;
    @Getter private String artistName;
    @Getter private double price;
}
