package com.jf.sc2022.dal.service;

import com.jf.sc2022.dal.dao.PaymentRepository;
import com.jf.sc2022.dal.model.Payment;
import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.dal.service.exceptions.SCPaymentFailureException;
import com.jf.sc2022.dto.ImageListingDTO;
import com.jf.sc2022.dto.PaymentDTO;
import com.jf.sc2022.dto.UserDTO;
import com.jf.sc2022.dto.payment.PaymentRequestDTO;
import com.jf.sc2022.dto.payment.PaymentResponseDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Coupon;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final UserService         userService;
    private final PaymentRepository   repository;
    private final ConversionService   mvcConversionService;
    private final ImageListingService imageListingService;
    private final EmailService        emailService;

    @Value("${stripe.keys.secret}")
    private String API_SECRET_KEY;

    @PostConstruct
    public void init() {
        Stripe.apiKey = API_SECRET_KEY;
    }

    public PaymentDTO createCharge(final String email, final String token, final int amount, final String currency) {
        final User user = userService.getUserFromContext();

        final Payment payment = Payment.builder()
                                       .email(email)
                                       .amount(amount)
                                       .chargeId(validateAndRetrieveCharge(token, amount, currency))
                                       .build();

        user.getTransactionHistory().add(payment);
//        userService.updateUser(user); Need to set this up to save the payment charge
        return mvcConversionService.convert(repository.save(payment), PaymentDTO.class);
    }

    private String validateAndRetrieveCharge(final String token, final int amount, final String currency) {
        try {
            Stripe.apiKey = API_SECRET_KEY;
            final Map<String, Object> details = new HashMap<>();
            details.put("amount", amount);
            details.put("description", "This is a test of the stripe payment process");
            details.put("source", token);
            details.put("currency", currency);

            final Charge charge = Charge.create(details);
            return charge.getId();
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createCustomer(final UserDTO user) {
        Stripe.apiKey = API_SECRET_KEY;

    }

    private Coupon retrieveCoupon(final String code) {
        try {
            Stripe.apiKey = API_SECRET_KEY;
            return Coupon.retrieve(code);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PaymentResponseDTO createPayment(final PaymentRequestDTO paymentRequestDTO) {
        final PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                                                                          .setAmount(paymentRequestDTO.getAmount() * 100L)
                                                                          .setCurrency(paymentRequestDTO.getCurrency())
                                                                          .build();

        final UserDTO         buyer           = userService.getUser(paymentRequestDTO.getUserId());
        final ImageListingDTO imageListingDTO = imageListingService.getImageListing(paymentRequestDTO.getImageListingId());
        final UserDTO         artist          = userService.getUser(imageListingDTO.getUserId());


        try {
            final PaymentIntent intent = PaymentIntent.create(params);
            final PaymentResponseDTO responseDTO = PaymentResponseDTO.builder()
                                                                     .clientSecret(intent.getClientSecret())
                                                                     .artistName(artist.getFirstName())
                                                                     .clientName(buyer.getFirstName())
                                                                     .listingTitle(paymentRequestDTO.getImageListingId())
                                                                     .build();

            emailService.handlePaymentEvent(buyer, artist, responseDTO);
            return responseDTO;
        } catch (final StripeException e) {
            throw new SCPaymentFailureException(e.getMessage());
        }


    }
}
