package com.jf.sc2022.dal.service;

import com.jf.sc2022.dal.dao.ConfirmationTokenRepository;
import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.dal.service.exceptions.SCUserAlreadyExistsException;
import com.jf.sc2022.dto.UserDTO;
import com.jf.sc2022.dto.login.LoginRequestDTO;
import com.jf.sc2022.dto.login.LoginResponseDTO;
import com.jf.sc2022.dto.registration.RegistrationDTO;
import com.jf.sc2022.helper.RegistrationHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpSession;
import java.util.Objects;

import static com.jf.sc2022.dal.service.AuthenticationService.USER_ALREADY_EXISTS_ERROR_MESSAGE;
import static com.jf.sc2022.helper.UserHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock private ConversionService           mvcConversionService;
    @Mock private PasswordEncoder             passwordEncoder;
    @Mock private UserService                 userService;
    @Mock private ConfirmationTokenRepository confirmationTokenRepository;
    @Mock private EmailService                emailService;
    @Mock private AuthenticationProvider      authenticationProvider;

    private AuthenticationService classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new AuthenticationService(mvcConversionService, passwordEncoder, userService, confirmationTokenRepository, emailService, authenticationProvider);
    }

    private static final RegistrationDTO REGISTRATION_DTO = RegistrationHelper.buildRegistrationDTO();
    private static final String          ENCODED_PASSWORD = "encodedString";

    @Test
    void testCreateUser_givenUserDoesntAlreadyExist() {
        when(mvcConversionService.convert(REGISTRATION_DTO, User.class)).thenReturn(createUser());
        when(passwordEncoder.encode(REGISTRATION_DTO.getPassword())).thenReturn(ENCODED_PASSWORD);
        when(userService.createUser(any(User.class))).thenReturn(createUserDTO());

        final UserDTO dto = classUnderTest.registerUser(REGISTRATION_DTO);
        Assertions.assertNotNull(dto);
    }

    @Test
    void testCreateUser_givenUserAlreadyExists() {
        when(userService.emailExists(anyString())).thenReturn(true);
        final SCUserAlreadyExistsException exception = Assertions.assertThrows(SCUserAlreadyExistsException.class,
                                                                               () -> classUnderTest.registerUser(REGISTRATION_DTO));
        Assertions.assertEquals(USER_ALREADY_EXISTS_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void testLogin_givenValidCredentials() {
        final LoginRequestDTO        loginRequest = buildLoginRequest();
        final MockHttpServletRequest request      = new MockHttpServletRequest();

        when(userService.getUserByUsername(USERNAME)).thenReturn(createUserDTO());

        final HttpSession      session  = request.getSession(true);
        final LoginResponseDTO response = classUnderTest.login(request, loginRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(createUserDTO(), response.getUser());
        Assertions.assertEquals(Objects.requireNonNull(session).getId(), response.getToken());
    }

    @Test
    void testLogin_givenInvalidCredentials() {
        final LoginRequestDTO        loginRequest = buildLoginRequest();
        final MockHttpServletRequest request      = new MockHttpServletRequest();

        when(authenticationProvider.authenticate(any())).thenThrow(BadCredentialsException.class);

        Assertions.assertThrows(BadCredentialsException.class, () -> classUnderTest.login(request, loginRequest));
    }

    private static LoginRequestDTO buildLoginRequest() {
        return LoginRequestDTO.builder()
                              .username(USERNAME)
                              .password(PASSWORD)
                              .build();
    }
}