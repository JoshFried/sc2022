package com.jf.sc2022.converter;

import com.jf.sc2022.dal.model.ImageListing;
import com.jf.sc2022.dto.ImageListingDTO;
import com.jf.sc2022.helper.ImageListingHelper;
import com.jf.sc2022.helper.UserHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageListingToImageListingDTOConverterTest {
    private ImageListingToImageListingDTOConverter classUnderTest;

    @BeforeEach
    public void setup() {
        classUnderTest = new ImageListingToImageListingDTOConverter();
    }

    @Test
    void testConvertHappyPath() {
        final ImageListingDTO imageListingDTO = ImageListingHelper.buildImageListingDTO();
        final ImageListing    imageListing    = ImageListingHelper.buildImageListing();
        imageListingDTO.setUserId(1);
        imageListing.setUser(UserHelper.createUser());
        Assertions.assertEquals(imageListingDTO, classUnderTest.convert(imageListing));
    }
}