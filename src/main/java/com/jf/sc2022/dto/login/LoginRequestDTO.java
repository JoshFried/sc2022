package com.jf.sc2022.dto.login;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestDTO {
    private String username;
    private String password;
}
