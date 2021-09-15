package com.jf.sc2022.dal.service;

import com.jf.sc2022.dal.dao.UserRepository;
import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.dto.UserDTO;
import com.jf.sc2022.helper.UserHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.Optional;

import static com.jf.sc2022.helper.UserHelper.EMAIL;
import static com.jf.sc2022.helper.UserHelper.USERNAME;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock private ConversionService mvcConversionService;
    @Mock private UserRepository    userRepository;
    private       UserService       classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new UserService(mvcConversionService, userRepository);
    }

    @Test
    void testCreateUser() {
        final User user = UserHelper.createUser();
        user.setPassword(null);
        final UserDTO userDTO = UserHelper.createUserDTO();

        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(mvcConversionService.convert(user, UserDTO.class)).thenReturn(userDTO);

        Assertions.assertEquals(userDTO, classUnderTest.createUser(user));
    }

    @Test
    void testUserNameExistsWhenTrue() {
        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.ofNullable(UserHelper.createUser()));

        Assertions.assertTrue(classUnderTest.usernameExists(USERNAME));
    }

    @Test
    void testUserNameExistsWhenNotTrue() {
        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        Assertions.assertFalse(classUnderTest.usernameExists(USERNAME));
    }

    @Test
    void emailExistsWhenTrue() {
        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(UserHelper.createUser()));

        Assertions.assertTrue(classUnderTest.emailExists(EMAIL));
    }

    @Test
    void testEmailExistsWhenFalse() {
        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        Assertions.assertFalse(classUnderTest.emailExists(EMAIL));
    }

    @Test
    void toggleAccountActivationWhenActive() {
        final User user = UserHelper.createUser();
        user.setEnabled(false);
        Mockito.when(userRepository.save(UserHelper.createUser())).thenReturn(user);

        final UserDTO userDTO = UserHelper.createUserDTO();
        userDTO.setEnabled(false);
        Mockito.when(mvcConversionService.convert(user, UserDTO.class)).thenReturn(userDTO);

        Assertions.assertFalse(classUnderTest.toggleAccountActivation(user).isEnabled());
    }

    @Test
    void toggleAccountActivationWhenNotActive() {
        final User before = UserHelper.createUser();
        final User after  = UserHelper.createUser();

        before.setEnabled(false);

        Mockito.when(userRepository.save(before)).thenReturn(after);
        Mockito.when(mvcConversionService.convert(before, UserDTO.class)).thenReturn(UserHelper.createUserDTO());

        Assertions.assertTrue(classUnderTest.toggleAccountActivation(before).isEnabled());
    }
}