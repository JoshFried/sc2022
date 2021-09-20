package com.jf.sc2022.dal.dao.mapper;


import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.dto.UserDTO;
import com.jf.sc2022.dto.registration.validators.FieldValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User mapStringFields(final UserDTO dto, final User user) {
        if (!dto.getEmail().equals(user.getEmail())) {
            FieldValidator.validateField("Email", dto.getEmail());
            user.setEmail(dto.getEmail());
        }

        if (!dto.getFirstName().equals(user.getFirstName())) {
            FieldValidator.validateField("First name", dto.getFirstName());
            user.setFirstName(dto.getFirstName());
        }

        if (!dto.getLastName().equals(user.getLastName())) {
            FieldValidator.validateField("Last name", dto.getLastName());
            user.setLastName(dto.getLastName());
        }

        if (!dto.getUsername().equals(user.getUsername())) {
            FieldValidator.validateField("Username", dto.getUsername());
            user.setUsername(dto.getUsername());
        }

        return user;
    }
}
