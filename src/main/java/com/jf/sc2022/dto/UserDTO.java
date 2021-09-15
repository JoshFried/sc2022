package com.jf.sc2022.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class UserDTO {
    private Long    id;
    private String  username;
    private String  email;
    private String  firstName;
    private String  lastName;
    private boolean isEnabled;
}
