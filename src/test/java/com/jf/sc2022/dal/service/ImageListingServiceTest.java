package com.jf.sc2022.dal.service;

import com.jf.sc2022.dal.dao.ImageListingRepository;
import com.jf.sc2022.dal.model.ImageListing;
import com.jf.sc2022.dal.model.User;
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

import java.util.Collections;

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


    //TODO: Get this to worok lul...
//    @Test
//    void testHandleBulkImagesWhenListIsEmpty() {
//        final BulkImageListingRequestDTO bulkImageListingRequestDTO = ImageListingHelper.buildBulkImageListingRequestDTO();
//        bulkImageListingRequestDTO.getImageListingRequestDTOList().remove(0);
//
//        Assertions.assertThrows(SCInvalidRequestException.class, () -> classUnderTest.handleBulkImages(bulkImageListingRequestDTO));
//    }

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
}