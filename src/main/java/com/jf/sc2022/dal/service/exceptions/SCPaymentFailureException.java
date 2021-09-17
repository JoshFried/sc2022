package com.jf.sc2022.dal.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SCPaymentFailureException extends RuntimeException {
    public SCPaymentFailureException(final String msg) {
        super(msg);
    }
}
