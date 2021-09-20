package com.jf.sc2022.dto.payment;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentResponseDTO {
    private long   id;
    private String clientName;
    private String listingTitle;
    private String artistName;
    private long   price;
}
