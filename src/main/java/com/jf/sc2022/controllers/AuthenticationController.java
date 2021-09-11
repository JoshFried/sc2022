package com.jf.sc2022.controllers;

import com.jf.sc2022.dal.service.AuthenticationService;
import com.jf.sc2022.dto.UserDTO;
import com.jf.sc2022.dto.login.LoginRequestDTO;
import com.jf.sc2022.dto.login.LoginResponseDTO;
import com.jf.sc2022.dto.registration.RegistrationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping(path = "/register")
    public ResponseEntity<UserDTO> addNewUser(@RequestBody final RegistrationDTO registrationDTO) {
        return new ResponseEntity<>(authenticationService.registerUser(registrationDTO), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponseDTO> loginUser(final HttpServletRequest request, @RequestBody final LoginRequestDTO loginRequest) {
        return new ResponseEntity<>(authenticationService.login(request, loginRequest), HttpStatus.OK);
    }

    @GetMapping(path = "/confirm-account")
    public ResponseEntity<UserDTO> confirmAccount(@RequestParam final String token) {
        return new ResponseEntity<>(authenticationService.confirmUserAccount(token), HttpStatus.ACCEPTED);
    }
}
