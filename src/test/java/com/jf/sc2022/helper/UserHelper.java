package com.jf.sc2022.helper;

import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.dto.UserDTO;

public class UserHelper {
    public static final Long   USER_ID    = 1L;
    public static final String USERNAME   = "someDude";
    public static final String PASSWORD   = "password";
    public static final String EMAIL      = "someemail@email.com";
    public static final String FIRST_NAME = "Joe";
    public static final String LAST_NAME  = "Smith";

    public static User createUser() {
        return User.builder()
                   .username(USERNAME)
                   .email(EMAIL)
                   .firstName(FIRST_NAME)
                   .id(USER_ID)
                   .lastName(LAST_NAME)
                   .password(PASSWORD)
                   .build();
    }

    public static User createBasicUser() {
        return User.builder()
                   .username(USERNAME)
                   .email(EMAIL)
                   .firstName(FIRST_NAME)
                   .lastName(LAST_NAME)
                   .build();
    }

    public static UserDTO createBasicUserDTO() {
        return UserDTO.builder()
                      .username(USERNAME)
                      .email(EMAIL)
                      .firstName(FIRST_NAME)
                      .lastName(LAST_NAME)
                      .build();
    }

    public static UserDTO createUserDTO() {
        return UserDTO.builder()
                      .username(USERNAME)
                      .email(EMAIL)
                      .firstName(FIRST_NAME)
                      .lastName(LAST_NAME)
                      .id(USER_ID)
                      .build();
    }
}
