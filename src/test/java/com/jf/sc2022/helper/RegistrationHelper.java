package com.jf.sc2022.helper;

import com.jf.sc2022.dto.registration.RegistrationDTO;

import static com.jf.sc2022.helper.UserHelper.*;

public class RegistrationHelper {

    public static RegistrationDTO buildRegistrationDTO() {
        return RegistrationDTO.builder()
                              .username(USERNAME)
                              .email(EMAIL)
                              .firstName(FIRST_NAME)
                              .lastName(LAST_NAME)
                              .password(PASSWORD)
                              .matchingPassword(PASSWORD)
                              .build();
    }
}
