package com.jf.sc2022.dto.login;

import com.jf.sc2022.dto.UserDTO;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseDTO {
    private String  token;
    private UserDTO user;
}

