package com.jf.sc2022.dal.service;

import com.jf.sc2022.dal.dao.PaymentRepository;
import com.jf.sc2022.dal.model.Payment;
import com.jf.sc2022.dal.service.exceptions.SCInvalidRequestException;
import com.jf.sc2022.dto.ImageListingDTO;
import com.jf.sc2022.dto.UserDTO;
import com.jf.sc2022.dto.payment.PaymentRequestDTO;
import com.jf.sc2022.dto.payment.PaymentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final UserService         userService;
    private final PaymentRepository   repository;
    private final ImageListingService imageListingService;
    private final EmailService        emailService;

    public PaymentResponseDTO createPayment(final PaymentRequestDTO paymentRequestDTO) {
        final ImageListingDTO imageListingDTO = imageListingService.getImageListing(paymentRequestDTO.getImageListingId());

        validatePaymentRequest(paymentRequestDTO, imageListingDTO);

        final UserDTO buyer  = userService.getUser(paymentRequestDTO.getUserId());
        final UserDTO artist = userService.getUser(imageListingDTO.getUserId());

        return getPaymentResponseDTO(buyer, imageListingDTO, artist);
    }

    private void validatePaymentRequest(final PaymentRequestDTO paymentRequestDTO, final ImageListingDTO imageListingDTO) {
        if (paymentRequestDTO.getAmount() != imageListingDTO.getPrice()) {
            throw new SCInvalidRequestException("Failed to complete transaction due to an invalid amount as part of the request");
        }

        if (!imageListingDTO.isAvailable()) {
            throw new SCInvalidRequestException("Failed to complete transaction, this listing is not currently for sale");
        }
    }

    private PaymentResponseDTO getPaymentResponseDTO(final UserDTO buyer, final ImageListingDTO imageListingDTO, final UserDTO artist) {
        final PaymentResponseDTO responseDTO = PaymentResponseDTO.builder()
                                                                 .artistName(artist.getUsername())
                                                                 .clientName(buyer.getUsername())
                                                                 .listingTitle(imageListingDTO.getTitle())
                                                                 .price(imageListingDTO.getPrice())
                                                                 .build();

        repository.save((Payment.builder().amount(imageListingDTO.getPrice()).imageListing(imageListingDTO.getId()).build()));
        emailService.handlePaymentEvent(buyer, artist, responseDTO);
        return responseDTO;
    }
}
