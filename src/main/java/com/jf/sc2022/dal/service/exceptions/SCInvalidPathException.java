package com.jf.sc2022.dal.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SCInvalidPathException extends RuntimeException {
    public SCInvalidPathException(final String msg) {
        super(msg);
    }
}
