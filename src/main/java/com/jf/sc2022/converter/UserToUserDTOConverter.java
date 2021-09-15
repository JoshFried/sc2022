package com.jf.sc2022.converter;


import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.dto.UserDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class UserToUserDTOConverter implements Converter<User, UserDTO> {
    @Override
    public UserDTO convert(final User user) {
        return UserDTO.builder()
                      .email(user.getEmail())
                      .username(user.getUsername())
                      .firstName(user.getFirstName())
                      .lastName(user.getLastName())
                      .isEnabled(user.isEnabled())
                      .build();
    }
}
