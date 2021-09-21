package com.jf.sc2022.dto.payment;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
@Data
public class PaymentResponseDTO {
    private String clientName;
    private String listingTitle;
    private String artistName;
    private double price;
}
