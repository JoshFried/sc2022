package com.jf.sc2022.dto.registration;

import com.jf.sc2022.dto.registration.validators.annotations.PasswordMatches;
import com.jf.sc2022.dto.registration.validators.annotations.ValidEmail;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@PasswordMatches
@ValidEmail
public class RegistrationDTO {
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String matchingPassword;
}
