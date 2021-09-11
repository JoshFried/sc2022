package com.jf.sc2022.dal.service;

import com.jf.sc2022.dal.dao.ConfirmationTokenRepository;
import com.jf.sc2022.dal.model.ConfirmationToken;
import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.dal.service.exceptions.ExceptionHelper;
import com.jf.sc2022.dal.service.exceptions.SCNotFoundException;
import com.jf.sc2022.dal.service.exceptions.SCUserAlreadyExistsException;
import com.jf.sc2022.dto.UserDTO;
import com.jf.sc2022.dto.registration.RegistrationDTO;
import com.jf.sc2022.dto.registration.validators.RegistrationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final ConversionService           mvcConversionService;
    private final PasswordEncoder             passwordEncoder;
    private final UserService                 userService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService                emailService;

    public static final String USER_ALREADY_EXISTS_ERROR_MESSAGE = "Username/email already exists";

    public UserDTO registerUser(final RegistrationDTO registrationDTO) {
        validateRegistrationDTO(registrationDTO);
        final User user = buildUserFromRegistrationDTO(registrationDTO);

        return userService.createUser(user);
    }

    public UserDTO confirmUserAccount(final String token) {
        final ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(token);

        if (confirmationToken == null) {
            throw new SCNotFoundException(ExceptionHelper.getNotFoundExceptionMessage("Token", token));
        }

        return userService.toggleAccountActivation(confirmationToken.getUser());
    }

    private User buildUserFromRegistrationDTO(final RegistrationDTO registrationDTO) {
        final User user = mvcConversionService.convert(registrationDTO, User.class);
        Objects.requireNonNull(user).setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        emailService.createAndSendEmailConfirmation(user);
        return user;
    }

    private void validateRegistrationDTO(final RegistrationDTO registrationDTO) {
        if (userService.emailExists(registrationDTO.getEmail()) || userService.usernameExists(registrationDTO.getUsername())) {
            throw new SCUserAlreadyExistsException(USER_ALREADY_EXISTS_ERROR_MESSAGE);
        }
        RegistrationValidator.validateRegistration(registrationDTO);
    }
}
