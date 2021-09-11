package com.jf.sc2022.converter;

import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.dto.registration.RegistrationDTO;
import com.jf.sc2022.helper.RegistrationHelper;
import com.jf.sc2022.helper.UserHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationDTOToUserConverterTest {
    private RegistrationDTOToUserConverter classUnderTest;

    @BeforeEach
    public void setup() {
        classUnderTest = new RegistrationDTOToUserConverter();
    }

    @Test
    void testConvertHappyPath() {
        final RegistrationDTO input          = RegistrationHelper.buildRegistrationDTO();
        final User            expectedResult = UserHelper.createUser();
        expectedResult.setId(null);
        Assertions.assertEquals(expectedResult, classUnderTest.convert(input));
    }
}