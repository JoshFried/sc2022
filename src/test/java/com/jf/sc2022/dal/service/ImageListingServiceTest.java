package com.jf.sc2022.dal.service;

import com.jf.sc2022.dal.dao.ImageListingRepository;
import com.jf.sc2022.dal.model.ImageListing;
import com.jf.sc2022.dal.model.Tag;
import com.jf.sc2022.dal.model.User;
import com.jf.sc2022.dal.service.exceptions.SCInvalidRequestException;
import com.jf.sc2022.dto.BulkImageListingRequestDTO;
import com.jf.sc2022.dto.ImageListingDTO;
import com.jf.sc2022.dto.ImageListingRequestDTO;
import com.jf.sc2022.helper.ImageListingHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static com.jf.sc2022.helper.ImageListingHelper.PRICE;

@ExtendWith(MockitoExtension.class)
class ImageListingServiceTest {
    @Mock private UserService            userService;
    @Mock private ConversionService      mvcConversionService;
    @Mock private ImageListingRepository imageListingRepository;
    private       ImageListingService    classUnderTest;

    @BeforeEach
    public void setup() {
        classUnderTest = new ImageListingService(userService, mvcConversionService, imageListingRepository);
    }

    @Test
    void testHandleBulkImagesHappyPath() {
        final BulkImageListingRequestDTO bulkImageListingRequestDTO = ImageListingHelper.buildBulkImageListingRequestDTO();
        final ImageListing               imageListing               = ImageListingHelper.buildImageListing();
        final User                       user                       = Mockito.mock(User.class);
        final ImageListingDTO            imageListingDTO            = ImageListingHelper.buildImageListingDTO();
        final MockMultipartFile          multipartFile              = Mockito.mock(MockMultipartFile.class);

        bulkImageListingRequestDTO.getImageListingRequestDTOList().get(0).setMultipartFile(multipartFile);

        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("td.png");
        Mockito.when(mvcConversionService.convert(bulkImageListingRequestDTO.getImageListingRequestDTOList().get(0), ImageListing.class)).thenReturn(imageListing);
        Mockito.when(userService.getUserFromContext()).thenReturn(user);
        Mockito.when(imageListingRepository.save(imageListing)).thenReturn(imageListing);
        Mockito.when(mvcConversionService.convert(imageListing, ImageListingDTO.class)).thenReturn(imageListingDTO);

        Assertions.assertEquals(classUnderTest.handleBulkImages(bulkImageListingRequestDTO), Collections.singletonList(imageListingDTO));
        Mockito.verify(userService).updateUsersListings(user, imageListing);
    }

    @Test
    void testHandleBulkImagesWhenListIsEmpty() {
        final BulkImageListingRequestDTO bulkImageListingRequestDTO = ImageListingHelper.buildBulkImageListingRequestDTO();
        bulkImageListingRequestDTO.setImageListingRequestDTOList(new ArrayList<>());

        Assertions.assertThrows(SCInvalidRequestException.class, () -> classUnderTest.handleBulkImages(bulkImageListingRequestDTO));
    }

    @Test
    void testHandleImageListingHappyPath() {
        final ImageListing           imageListing           = ImageListingHelper.buildImageListing();
        final User                   user                   = Mockito.mock(User.class);
        final ImageListingDTO        imageListingDTO        = ImageListingHelper.buildImageListingDTO();
        final MockMultipartFile      multipartFile          = Mockito.mock(MockMultipartFile.class);
        final ImageListingRequestDTO imageListingRequestDTO = ImageListingHelper.buildImageListingRequestDTO();
        imageListingRequestDTO.setMultipartFile(multipartFile);

        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("td.png");
        Mockito.when(mvcConversionService.convert(imageListingRequestDTO, ImageListing.class)).thenReturn(imageListing);
        Mockito.when(userService.getUserFromContext()).thenReturn(user);
        Mockito.when(imageListingRepository.save(imageListing)).thenReturn(imageListing);
        Mockito.when(mvcConversionService.convert(imageListing, ImageListingDTO.class)).thenReturn(imageListingDTO);
        Assertions.assertEquals(classUnderTest.handleImageListing(imageListingRequestDTO), imageListingDTO);

        Mockito.verify(userService).updateUsersListings(user, imageListing);
    }

    @Test
    void testUpdateImageListingWithNewPrice() {
        final ImageListing    imageListing    = ImageListingHelper.buildImageListing();
        final ImageListingDTO imageListingDTO = ImageListingHelper.buildImageListingDTO();
        imageListingDTO.setPrice(PRICE + 1.0);

        final ImageListing expectedResult = ImageListingHelper.buildImageListing();
        expectedResult.setPrice(PRICE + 1.0);

        Mockito.when(imageListingRepository.findById(imageListingDTO.getId())).thenReturn(Optional.ofNullable(imageListing));
        Mockito.when(imageListingRepository.save(expectedResult)).thenReturn(expectedResult);
        Mockito.when(mvcConversionService.convert(expectedResult, ImageListingDTO.class)).thenReturn(imageListingDTO);

        assertResult(imageListingDTO);
    }

    @Test
    void testUpdateImageListingWithNewTags() {
        final ImageListing    imageListing    = ImageListingHelper.buildImageListing();
        final ImageListingDTO imageListingDTO = ImageListingHelper.buildImageListingDTO();
        final ImageListing    expectedResult  = ImageListingHelper.buildImageListing();

        final Tag nature = new Tag("nature");
        imageListingDTO.getTags().add(nature);
        expectedResult.getTags().add(nature);

        Mockito.when(imageListingRepository.findById(imageListingDTO.getId())).thenReturn(Optional.ofNullable(imageListing));
        Mockito.when(imageListingRepository.save(expectedResult)).thenReturn(expectedResult);
        Mockito.when(mvcConversionService.convert(expectedResult, ImageListingDTO.class)).thenReturn(imageListingDTO);

        assertResult(imageListingDTO);
    }

    @Test
    void testUpdateImageListingWithUpdatedViews() {
        final ImageListing    imageListing    = ImageListingHelper.buildImageListing();
        final ImageListingDTO imageListingDTO = ImageListingHelper.buildImageListingDTO();
        final ImageListing    expectedResult  = ImageListingHelper.buildImageListing();

        final long views = imageListing.getViews() + 1;
        imageListingDTO.setViews(views);
        expectedResult.setViews(views);

        Mockito.when(imageListingRepository.findById(imageListingDTO.getId())).thenReturn(Optional.ofNullable(imageListing));
        Mockito.when(imageListingRepository.save(expectedResult)).thenReturn(expectedResult);
        Mockito.when(mvcConversionService.convert(expectedResult, ImageListingDTO.class)).thenReturn(imageListingDTO);

        assertResult(imageListingDTO);
    }

    @Test
    void testUpdateImageListingWithChangedAvailability() {
        final ImageListing    imageListing    = ImageListingHelper.buildImageListing();
        final ImageListingDTO imageListingDTO = ImageListingHelper.buildImageListingDTO();
        final ImageListing    expectedResult  = ImageListingHelper.buildImageListing();

        imageListingDTO.setAvailable(true);
        expectedResult.setAvailable(true);

        Mockito.when(imageListingRepository.findById(imageListingDTO.getId())).thenReturn(Optional.ofNullable(imageListing));
        Mockito.when(imageListingRepository.save(expectedResult)).thenReturn(expectedResult);
        Mockito.when(mvcConversionService.convert(expectedResult, ImageListingDTO.class)).thenReturn(imageListingDTO);

        assertResult(imageListingDTO);
    }

    private void assertResult(final ImageListingDTO imageListingDTO) {
        final ImageListingDTO result = classUnderTest.updateImageListing(imageListingDTO);
        Assertions.assertEquals(imageListingDTO, result);
        Assertions.assertNotEquals(ImageListingHelper.buildImageListingDTO(), result);
    }
}