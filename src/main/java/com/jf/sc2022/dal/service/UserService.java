package com.jf.sc2022.dal.service;

import com.jf.sc2022.dal.dao.UserRepository;
import com.jf.sc2022.dal.model.ImageListing;
import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.dal.service.exceptions.ExceptionHelper;
import com.jf.sc2022.dal.service.exceptions.SCNotFoundException;
import com.jf.sc2022.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ConversionService mvcConversionService;
    private final UserRepository    userRepository;

    public User getUserFromContext() {
        final UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return fetchByUsername(details.getUsername());
    }

    public UserDTO createUser(final User user) {
        return mvcConversionService.convert(userRepository.save(user), UserDTO.class);
    }

    public boolean usernameExists(final String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean emailExists(final String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public UserDTO toggleAccountActivation(final User user) {
        user.setEnabled(!user.isEnabled());
        return mvcConversionService.convert(userRepository.save(user), UserDTO.class);
    }

    public UserDTO getUserByUsername(final String username) {
        return mvcConversionService.convert(fetchByUsername(username), UserDTO.class);
    }

    public User fetchByUsername(final String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new SCNotFoundException(ExceptionHelper.getNotFoundExceptionMessage("Username", username)));
    }

    public void updateUsersListings(final User user, final ImageListing listing) {
        user.getListings().add(listing);
        userRepository.save(user);
    }
}
