package com.jf.sc2022.dal.service.exceptions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionHelper {
    public static String getNotFoundExceptionMessage(final String field, final String parameter) {
        return String.format("%s: %s doesn't exist", field, parameter);
    }
}
