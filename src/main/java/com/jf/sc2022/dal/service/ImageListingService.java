package com.jf.sc2022.dal.service;

import com.jf.sc2022.dal.dao.ImageListingRepository;
import com.jf.sc2022.dal.dao.mapper.ImageListingMapper;
import com.jf.sc2022.dal.dao.mapper.TagRepository;
import com.jf.sc2022.dal.model.ImageListing;
import com.jf.sc2022.dal.model.Tag;
import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.dal.service.exceptions.SCInvalidPathException;
import com.jf.sc2022.dal.service.exceptions.SCInvalidRequestException;
import com.jf.sc2022.dal.service.exceptions.SCNotFoundException;
import com.jf.sc2022.dto.BulkImageListingRequestDTO;
import com.jf.sc2022.dto.ImageListingDTO;
import com.jf.sc2022.dto.ImageListingRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageListingService {
    private final UserService            userService;
    private final ConversionService      mvcConversionService;
    private final ImageListingRepository repository;
    private final TagRepository          tagRepository;

    private static final String STORAGE_DIRECTORY = "src/main/resources/storage";

    /**
     * Handle a bulk image request, first validate the request -> a valid request contains at least one new ImageListing
     *
     * @param bulkImageListingRequestDTO ImageListingRequest containing multiple ImageListingRequestDTO objects
     * @return ImageListingDTO representing the newly saved object in our database
     */
    public List<ImageListingDTO> handleBulkImages(final BulkImageListingRequestDTO bulkImageListingRequestDTO) {
        if (!validateRequest(bulkImageListingRequestDTO)) {
            throw new SCInvalidRequestException("Input is invalid, you must upload at least 1 image");
        }

        return bulkImageListingRequestDTO.getImageListingRequestDTOList()
                                         .stream()
                                         .map(this::handleImageListing)
                                         .collect(Collectors.toList());
    }

    /**
     * @param requestDTO ImageListingRequest containing all the information to create our new ImageListing object and save to the DB
     * @return ImageListingDTO representing the newly saved object in our database
     */
    public ImageListingDTO handleImageListing(final ImageListingRequestDTO requestDTO) {
        final String path      = createPath(requestDTO.getMultipartFile());
        final String logPrefix = "ImageListingService [buildImageListingDTO]:";

        log.info(String.format("%s attempting to create image listing %s", logPrefix, requestDTO.getTitle()));

        if (path == null) {
            log.error(String.format("%s invalid path, skipping this image listing", logPrefix));
            throw new SCInvalidPathException("Path is invalid");
        }

        return insertImageListing(requestDTO, path);
    }

    public ImageListingDTO getImageListing(final long id) {
        return mvcConversionService.convert(fetchImageListing(id), ImageListingDTO.class);
    }

    public ImageListingDTO updateImageListing(final ImageListingDTO imageListingDTO) {
        final ImageListing imageListing = fetchImageListing(imageListingDTO.getId());

        final ImageListing updatedListing = ImageListingMapper.updateImageFields(imageListingDTO, imageListing);
        return mvcConversionService.convert(repository.save(updatedListing), ImageListingDTO.class);
    }

    public ImageListingDTO deleteImageListing(final long id) {
        final ImageListing imageListing = fetchImageListing(id);
        final String       path         = imageListing.getPath();

        try {
            Files.deleteIfExists(Path.of(path));
            repository.deleteById(imageListing.getId());
            return mvcConversionService.convert(imageListing, ImageListingDTO.class);
        } catch (final IOException e) {
            log.error(String.format("ImageListingService [deleteImageListing]: failed to delete image listing due to an invalid path %s", path));
            throw new SCInvalidPathException(String.format("Cannot find path: %s", path));
        }
    }

    public Set<ImageListingDTO> searchByTag(final Tag tag) {
        return convertBulkImageListings(repository.findAllByTags(tag));
    }

    public Set<ImageListingDTO> searchByTags(final Set<Tag> tags) {
        final Set<ImageListingDTO> result = new HashSet<>();
        tags.stream().map(this::searchByTag).forEach(result::addAll);
        return result;
    }

    public Set<ImageListingDTO> searchByTitle(final String title) {
        return convertBulkImageListings(repository.findAllByTitleLike(title));
    }

    public Set<ImageListingDTO> searchByDescription(final String query) {
        return convertBulkImageListings(repository.findAllByDescriptionLike(query));
    }

    private Set<ImageListingDTO> convertBulkImageListings(final Set<ImageListing> listings) {
        return listings.stream()
                       .map(imageListing -> mvcConversionService.convert(imageListing, ImageListingDTO.class))
                       .collect(Collectors.toSet());
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

        listing.getTags()
               .stream()
               .filter(tag -> tagRepository.findById(tag.getName()).isEmpty())
               .forEach(tagRepository::save);

        userService.updateUsersListings(user, listing);
        return mvcConversionService.convert(repository.save(listing), ImageListingDTO.class);
    }

    /**
     * @param multipartFile file containing the information needed to create a path
     * @return the path where the new image will be saved
     */
    private String createPath(final MultipartFile multipartFile) {
        return validatePath(multipartFile);
    }

    /**
     * @param multipartFile file containing the information that will be stored in our newly created local file
     * @return path of our newly created file
     */
    private String validatePath(final MultipartFile multipartFile) {
        try {
            final Path path = Paths.get(STORAGE_DIRECTORY).resolve(String.format("%s%s", UUID.randomUUID(), multipartFile.getOriginalFilename()));
            Files.copy(multipartFile.getInputStream(), path);
            return path.toString();
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
                       || !bulkImageListingRequestDTO.getImageListingRequestDTOList().isEmpty();
    }

    private ImageListing fetchImageListing(final long id) {
        return repository.findById(id).orElseThrow(() -> new SCNotFoundException("ImageListing with id: " + id + " does not exist"));
    }
}
