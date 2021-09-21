package com.jf.sc2022.dal.service;

import com.jf.sc2022.dal.dao.PaymentRepository;
import com.jf.sc2022.dal.model.Payment;
import com.jf.sc2022.dal.service.exceptions.SCInvalidRequestException;
import com.jf.sc2022.dto.ImageListingDTO;
import com.jf.sc2022.dto.UserDTO;
import com.jf.sc2022.dto.payment.PaymentRequestDTO;
import com.jf.sc2022.dto.payment.PaymentResponseDTO;
import com.jf.sc2022.helper.ImageListingHelper;
import com.jf.sc2022.helper.UserHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.jf.sc2022.helper.ImageListingHelper.IMAGE_LISTING_ID;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock private UserService         userService;
    @Mock private PaymentRepository   paymentRepository;
    @Mock private ImageListingService imageListingService;
    @Mock private EmailService        emailService;

    private PaymentService classUnderTest;

    @BeforeEach
    void setup() {
        classUnderTest = new PaymentService(userService, paymentRepository, imageListingService, emailService);
    }

    @Test
    void testCreatePaymentHappyPath() {
        final PaymentRequestDTO  paymentRequestDTO  = ImageListingHelper.buildPaymentRequestDTO();
        final PaymentResponseDTO paymentResponseDTO = ImageListingHelper.buildPaymentResponseDTO();
        final UserDTO            userDTO            = UserHelper.createUserDTO();
        final ImageListingDTO    imageListingDTO    = ImageListingHelper.buildImageListingDTO();
        imageListingDTO.setAvailable(true);
        imageListingDTO.setUserId(UserHelper.USER_ID);

        Mockito.when(userService.getUser(UserHelper.USER_ID)).thenReturn(userDTO);
        Mockito.when(imageListingService.getImageListing(IMAGE_LISTING_ID)).thenReturn(imageListingDTO);
        Assertions.assertEquals(paymentResponseDTO, classUnderTest.createPayment(paymentRequestDTO));

        Mockito.verify(paymentRepository).save(Payment.builder()
                                                      .amount(paymentRequestDTO.getAmount())
                                                      .imageListing(paymentRequestDTO.getImageListingId())
                                                      .build());
        Mockito.verify(emailService).handlePaymentEvent(userDTO, userDTO, paymentResponseDTO);
    }

    @Test
    void testCreatePaymentWhenNotAvailable() {
        final PaymentRequestDTO paymentRequestDTO = ImageListingHelper.buildPaymentRequestDTO();
        final ImageListingDTO   imageListingDTO   = ImageListingHelper.buildImageListingDTO();

        Mockito.when(imageListingService.getImageListing(paymentRequestDTO.getImageListingId())).thenReturn(imageListingDTO);
        Assertions.assertThrows(SCInvalidRequestException.class, () -> classUnderTest.createPayment(paymentRequestDTO));
    }

    @Test
    void testCreatePaymentWhenAmountIsNotEnough() {
        final PaymentRequestDTO paymentRequestDTO = ImageListingHelper.buildPaymentRequestDTO();
        paymentRequestDTO.setAmount(0.5);
        final ImageListingDTO imageListingDTO = ImageListingHelper.buildImageListingDTO();

        Mockito.when(imageListingService.getImageListing(paymentRequestDTO.getImageListingId())).thenReturn(imageListingDTO);
        Assertions.assertThrows(SCInvalidRequestException.class, () -> classUnderTest.createPayment(paymentRequestDTO));
    }
}