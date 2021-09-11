package com.jf.sc2022.converter;

import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.helper.UserHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserToUserDTOConverterTest {
    UserToUserDTOConverter classUnderTest;

    @BeforeEach
    public void setup() {
        classUnderTest = new UserToUserDTOConverter();
    }

    @Test
    void testConvertHappyPath() {
        final User input = UserHelper.createBasicUser();
        Assertions.assertEquals(UserHelper.createBasicUserDTO(), classUnderTest.convert(input));
    }
}