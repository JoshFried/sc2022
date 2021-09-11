package com.jf.sc2022.dal.service;

import com.jf.sc2022.dal.dao.ImageListingRepository;
import com.jf.sc2022.dal.model.ImageListing;
import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.dto.ImageListingDTO;
import com.jf.sc2022.dto.ImageListingRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageListingService {
    private final UserService            userService;
    private final ConversionService      mvcConversionService;
    private final ImageListingRepository repository;

    private static final String STORAGE_DIRECTORY = "/src/main/resources/storage";

    private ImageListingDTO createAndSaveImageListing(final ImageListingRequestDTO listingRequestDTO, final String path) {
        final ImageListing listing = mvcConversionService.convert(listingRequestDTO, ImageListing.class);
        final User         user    = userService.getUserFromContext();
        listing.setUser(user);
        listing.setPath(path);

        userService.updateUsersListings(user, listing);
        return mvcConversionService.convert(repository.save(listing), ImageListingDTO.class);
    }
}
