package com.jf.sc2022.dto.registration.validators.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidFieldException extends RuntimeException {

    public InvalidFieldException(final String msg) {
        super(msg);
    }
}
