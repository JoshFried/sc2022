package com.jf.sc2022.dal.service;

import com.jf.sc2022.dal.dao.ConfirmationTokenRepository;
import com.jf.sc2022.dal.model.ConfirmationToken;
import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.dal.service.exceptions.SCNotFoundException;
import com.jf.sc2022.dal.service.exceptions.SCUserAlreadyExistsException;
import com.jf.sc2022.dto.UserDTO;
import com.jf.sc2022.dto.login.LoginRequestDTO;
import com.jf.sc2022.dto.login.LoginResponseDTO;
import com.jf.sc2022.dto.registration.RegistrationDTO;
import com.jf.sc2022.dto.registration.validators.RegistrationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationService {
    private final ConversionService           mvcConversionService;
    private final PasswordEncoder             passwordEncoder;
    private final UserService                 userService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService                emailService;
    private final AuthenticationProvider      authenticationProvider;

    public static final String USER_ALREADY_EXISTS_ERROR_MESSAGE = "Username/email already exists";

    private static final String AUTHENTICATION_SERVICE = "AuthenticationService";

    public UserDTO registerUser(final RegistrationDTO registrationDTO) {
        validateRegistrationDTO(registrationDTO);
        final ConfirmationToken token = getConfirmationToken(registrationDTO);

        log.info(AUTHENTICATION_SERVICE + " [registerUser]: saving user to database");
        final UserDTO user = userService.createUser(token.getUser());

        log.info(String.format("%s [registerUser]: sending confirm email to: %s with token: %s", AUTHENTICATION_SERVICE, user.getEmail(), token));
        confirmationTokenRepository.save(token);

        return user;
    }

    public UserDTO confirmUserAccount(final String token) {
        final ConfirmationToken confirmationToken = confirmationTokenRepository.findById(token).orElseThrow(() -> new SCNotFoundException(String.format("Token : %s is not valid", token)));

        log.info(String.format("%s [confirmUserAccount]: confirming account for user: %s", AUTHENTICATION_SERVICE, confirmationToken.getUser().getUsername()));
        return userService.toggleAccountActivation(confirmationToken.getUser());
    }


    public LoginResponseDTO login(final HttpServletRequest request, final LoginRequestDTO loginRequest) {
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                                                                                                                loginRequest.getPassword());
        authenticationProvider.authenticate(authenticationToken);
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authenticationToken);

        final HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);

        return LoginResponseDTO.builder()
                               .token(session.getId())
                               .user(userService.getUserByUsername(loginRequest.getUsername()))
                               .build();
    }

    private ConfirmationToken getConfirmationToken(final RegistrationDTO registrationDTO) {
        final User user = mvcConversionService.convert(registrationDTO, User.class);
        Objects.requireNonNull(user).setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        return emailService.sendConfirmationEmail(user);
    }

    private void validateRegistrationDTO(final RegistrationDTO registrationDTO) {
        if (userService.emailExists(registrationDTO.getEmail()) || userService.usernameExists(registrationDTO.getUsername())) {
            log.error(String.format("%s [validateRegistrationDTO]: unable to validate user: %s", AUTHENTICATION_SERVICE, registrationDTO.getUsername()));
            throw new SCUserAlreadyExistsException(USER_ALREADY_EXISTS_ERROR_MESSAGE);
        }

        log.info(String.format("%s [validateRegistrationDTO]: successfully validated user: %s", AUTHENTICATION_SERVICE, registrationDTO.getUsername()));
        RegistrationValidator.validateRegistration(registrationDTO);
    }
}
