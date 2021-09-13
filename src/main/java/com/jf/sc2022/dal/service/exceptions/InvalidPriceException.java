package com.jf.sc2022.dal.service.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidPriceException extends RuntimeException {
    public InvalidPriceException(final String msg) {
        super(msg);
    }
}
