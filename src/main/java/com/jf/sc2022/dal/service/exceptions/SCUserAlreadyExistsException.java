package com.jf.sc2022.dal.service.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SCUserAlreadyExistsException extends RuntimeException {
    public SCUserAlreadyExistsException(final String msg) {
        super(msg);
    }
}
