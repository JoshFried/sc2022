package com.jf.sc2022.converter;


import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.dto.registration.RegistrationDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class RegistrationDTOToUserConverter implements Converter<RegistrationDTO, User> {

    @Override
    public User convert(final RegistrationDTO registrationDTO) {
        return User.builder()
                   .email(registrationDTO.getEmail())
                   .username(registrationDTO.getUsername())
                   .password(registrationDTO.getPassword())
                   .firstName(registrationDTO.getFirstName())
                   .lastName(registrationDTO.getLastName())
                   .build();
    }
}
