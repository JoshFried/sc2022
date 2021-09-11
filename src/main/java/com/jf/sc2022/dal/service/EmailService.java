package com.jf.sc2022.dal.service;

import com.jf.sc2022.dal.model.ConfirmationToken;
import com.jf.sc2022.dal.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {
    private JavaMailSender mailSender;

    public void createAndSendEmailConfirmation(final User user) {
        final ConfirmationToken token = new ConfirmationToken(user);

        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Email Confirmation");
        email.setFrom("sc2021jf@gmail.com");
        email.setText("To confirm your email click here : "
                              + "http://localhost:8080/confirm-account?token=" + token.getToken());
        mailSender.send(email);
    }
}