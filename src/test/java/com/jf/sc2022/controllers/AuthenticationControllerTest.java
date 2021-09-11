package com.jf.sc2022.controllers;

import com.jf.sc2022.dal.service.AuthenticationService;
import com.jf.sc2022.dto.UserDTO;
import com.jf.sc2022.dto.registration.RegistrationDTO;
import com.jf.sc2022.helper.RegistrationHelper;
import com.jf.sc2022.helper.UserHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {
    @Mock private AuthenticationService    authenticationService;
    private       AuthenticationController classUnderTest;

    @BeforeEach
    public void setup() {
        classUnderTest = new AuthenticationController(authenticationService);
    }

    @Test
    void testRegisterNewUserHappyPath() {
        final RegistrationDTO registrationDTO = RegistrationHelper.buildRegistrationDTO();
        final UserDTO         userDTO         = UserHelper.createBasicUserDTO();
        Mockito.when(authenticationService.registerUser(registrationDTO)).thenReturn(userDTO);

        Assertions.assertEquals(new ResponseEntity<>(userDTO, HttpStatus.CREATED), classUnderTest.addNewUser(registrationDTO));
    }

    @Test
    void testConfirmAccountHappyPath() {
        final String  token   = "1";
        final UserDTO userDTO = UserHelper.createUserDTO();
        Mockito.when(authenticationService.confirmUserAccount(token)).thenReturn(userDTO);

        Assertions.assertEquals(new ResponseEntity<>(userDTO, HttpStatus.ACCEPTED), classUnderTest.confirmAccount(token));
    }
}