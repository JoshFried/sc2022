package com.jf.sc2022.dal.service;

import com.jf.sc2022.dal.dao.ImageListingRepository;
import com.jf.sc2022.dal.model.ImageListing;
import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.dal.service.exceptions.SCInvalidPathException;
import com.jf.sc2022.dal.service.exceptions.SCNotFoundException;
import com.jf.sc2022.dto.BulkImageListingRequestDTO;
import com.jf.sc2022.dto.ImageListingDTO;
import com.jf.sc2022.dto.ImageListingRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageListingService {
    private final UserService            userService;
    private final ConversionService      mvcConversionService;
    private final ImageListingRepository repository;

    private static final String STORAGE_DIRECTORY = "/src/main/resources/storage";

    /**
     * @param bulkImageListingRequestDTO ImageListingRequest containing multiple ImageListingRequestDTO objects
     * @return ImageListingDTO representing the newly saved object in our database
     */
    public List<ImageListingDTO> handleBulkImages(final BulkImageListingRequestDTO bulkImageListingRequestDTO) {
        if (!validateRequest(bulkImageListingRequestDTO)) {
            throw new SCNotFoundException("Input is invalid, you must upload at least 1 image");
        }

        return bulkImageListingRequestDTO.getImageListingRequestDTOList().stream()
                                         .map(this::handleImageListing)
                                         .collect(Collectors.toList());
    }

    /**
     * @param requestDTO ImageListingRequest containing all the information to create our new ImageListing object and save to the DB
     * @return ImageListingDTO representing the newly saved object in our database
     */
    private ImageListingDTO handleImageListing(final ImageListingRequestDTO requestDTO) {
        final String path      = createPath(requestDTO.getMultipartFile());
        final String logPrefix = "ImageListingService [buildImageListingDTO]:";

        log.info(String.format("%s attempting to create image listing %s", logPrefix, requestDTO.getTitle()));

        if (path == null) {
            log.error(String.format("%s invalid path, skipping this image listing", logPrefix));
            throw new SCInvalidPathException("Path is invalid");
        }

        return insertImageListing(requestDTO, path);
    }

    /**
     * @param requestDTO ImageListingRequest containing all the information to create our new ImageListing object and save to the DB
     * @param path       this is the path where the new image will be saved
     * @return ImageListingDTO representing the newly saved object in our database
     */
    private ImageListingDTO insertImageListing(final ImageListingRequestDTO requestDTO, final String path) {
        final ImageListing listing = mvcConversionService.convert(requestDTO, ImageListing.class);
        final User         user    = userService.getUserFromContext();
        listing.setUser(user);
        listing.setPath(path);

        userService.updateUsersListings(user, listing);
        return mvcConversionService.convert(repository.save(listing), ImageListingDTO.class);
    }

    /**
     * @param multipartFile file containing the information needed to create a path
     * @return the path where the new image will be saved
     */
    private String createPath(final MultipartFile multipartFile) {
        final String path = String.format("%s/%s", STORAGE_DIRECTORY, multipartFile.getOriginalFilename());
        return validatePath(multipartFile, path);
    }

    /**
     * @param multipartFile file containing the information that will be stored in our newly created local file
     * @param path          this is the path where the new image will be saved
     * @return path of our newly created file
     */
    private String validatePath(final MultipartFile multipartFile, final String path) {
        try {
            multipartFile.transferTo(new File(path));
            return path;
        } catch (final IOException e) {
            return null;
        }
    }

    /**
     * @param bulkImageListingRequestDTO ImageListingRequest containing multiple ImageListingRequestDTO objects
     * @return true iff the collection is not empty or null else false
     */
    private static boolean validateRequest(final BulkImageListingRequestDTO bulkImageListingRequestDTO) {
        return bulkImageListingRequestDTO.getImageListingRequestDTOList() == null
                       || bulkImageListingRequestDTO.getImageListingRequestDTOList().isEmpty();
    }
}
