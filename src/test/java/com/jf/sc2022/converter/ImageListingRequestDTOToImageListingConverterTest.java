package com.jf.sc2022.converter;

import com.jf.sc2022.dal.model.ImageListing;
import com.jf.sc2022.dal.model.Tag;
import com.jf.sc2022.dto.ImageListingRequestDTO;
import com.jf.sc2022.helper.ImageListingHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.jf.sc2022.helper.ImageListingHelper.TAG;

class ImageListingRequestDTOToImageListingConverterTest {
    private ImageListingRequestDTOToImageListingConverter classUnderTest;

    @BeforeEach
    public void setup() {
        classUnderTest = new ImageListingRequestDTOToImageListingConverter();
    }

    @Test
    void testConvertHappyPath() {
        final ImageListingRequestDTO requestDTO   = ImageListingHelper.buildImageListingRequestDTO();
        final ImageListing           imageListing = ImageListingHelper.buildBasicImageListing();
        imageListing.setTags(Collections.singleton(new Tag(TAG)));

        Assertions.assertEquals(imageListing, classUnderTest.convert(requestDTO));
    }
}