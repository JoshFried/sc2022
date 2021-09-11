package com.jf.sc2022.converter;

import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.dto.UserDTO;
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
        final User input = User.builder()
                               .username("joshf")
                               .email("joshf@gmail.com")
                               .firstName("josh")
                               .lastName("fried")
                               .build();

        Assertions.assertEquals(UserDTO.builder()
                                       .username("joshf")
                                       .email("joshf@gmail.com")
                                       .firstName("josh")
                                       .lastName("fried")
                                       .build(),
                                classUnderTest.convert(input));
    }
}