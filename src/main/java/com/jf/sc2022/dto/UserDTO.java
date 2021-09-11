package com.jf.sc2022.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long   id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
