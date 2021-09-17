package com.jf.sc2022.dal.service;

import com.jf.sc2022.dal.model.ConfirmationToken;
import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.dto.UserDTO;
import com.jf.sc2022.dto.payment.PaymentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    /**
     * After a user attempts to register send a confirmation email with the token, allowing a customer to enable their account
     *
     * @param user the new user that has been created
     */
    public void sendConfirmationEmail(final User user) {
        final ConfirmationToken token = new ConfirmationToken(user);

        final String content = String.format("To confirm your email click here : http://localhost:8080/confirm-account?token=%s", token.getConfirmationToken());
        getSimpleMailMessage(user.getEmail(), content);
    }

    /**
     * This method is triggered by the successful completion of a payment event, sending an email to both the customer and the artist notifying them of a successful transaction
     *
     * @param buyer              the user that bought the image listing
     * @param artist             the user that created the image listing
     * @param paymentResponseDTO object containing all the information about the transaction
     */
    public void handlePaymentEvent(final UserDTO buyer, final UserDTO artist, final PaymentResponseDTO paymentResponseDTO) {
        sendConfirmationEmail(buyer, paymentResponseDTO);
        sendOrderNotification(artist, paymentResponseDTO);
    }

    private void sendConfirmationEmail(final UserDTO userDTO, final PaymentResponseDTO responseDTO) {
        final String content = String.format("This email is to confirm your order of the listing titled: %s from artist: %s purchased for the price of: %f",
                                             responseDTO.getListingTitle(),
                                             responseDTO.getArtistName(),
                                             responseDTO.getPrice());

        getSimpleMailMessage(userDTO.getEmail(), content);
    }

    private void sendOrderNotification(final UserDTO userDTO, final PaymentResponseDTO paymentResponseDTO) {
        final String content = String.format("This email is to notify you of a purchasing order from user: %s for the listing titled: %s for a purchase price of :%f",
                                             paymentResponseDTO.getClientName(),
                                             paymentResponseDTO.getListingTitle(),
                                             paymentResponseDTO.getPrice());

        getSimpleMailMessage(userDTO.getEmail(), content);
    }

    private void getSimpleMailMessage(final String recipient, final String content) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipient);
        email.setSubject("Email Confirmation");
        email.setFrom("sc2021jf@gmail.com");
        email.setText(content);
        mailSender.send(email);
    }
}