package com.jf.sc2022.dto.registration.validators;


import com.jf.sc2022.dto.registration.validators.exceptions.InvalidFieldException;

public final class FieldValidator {
    private FieldValidator() {
    }

    public static void validateField(final String field, final String fieldName) {
        if (field.isEmpty()) {
            final String errorMsg = String.format("Invalid field: %s field cannot be empty", fieldName);
            throw new InvalidFieldException(errorMsg);
        }
    }
}

