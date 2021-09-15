package com.jf.sc2022.dal.service;

import com.jf.sc2022.dal.dao.PaymentRepository;
import com.jf.sc2022.dal.model.Payment;
import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.dto.PaymentDTO;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final UserService       userService;
    private final PaymentRepository repository;
    private final ConversionService mvcConversionService;

    @Value("${stripe.keys.secret}")
    private String API_SECRET_KEY;

    @PostConstruct
    public void init() {
        Stripe.apiKey = API_SECRET_KEY;
    }

    public PaymentDTO createCharge(final String email, final String token, final int amount) {
        final UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final User        user    = userService.fetchByUsername(details.getUsername());
        final Payment payment = Payment.builder()
                                       .email(email)
                                       .amount(amount)
                                       .chargeId(validateAndRetrieveCharge(token, amount))
                                       .build();

        user.getTransactionHistory().add(payment);
        return mvcConversionService.convert(repository.save(payment), PaymentDTO.class);
    }

    private String validateAndRetrieveCharge(final String token, final int amount) {
        try {
            Stripe.apiKey = API_SECRET_KEY;
            final Map<String, Object> details = new HashMap<>();
            details.put("amount", amount);
            details.put("description", "This is a test of the stripe payment process");
            details.put("source", token);

            final Charge charge = Charge.create(details);
            return charge.getId();
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
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
}
