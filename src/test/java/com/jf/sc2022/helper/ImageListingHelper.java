package com.jf.sc2022.helper;

import com.jf.sc2022.dal.model.ImageListing;
import com.jf.sc2022.dto.BulkImageListingRequestDTO;
import com.jf.sc2022.dto.ImageListingDTO;
import com.jf.sc2022.dto.ImageListingRequestDTO;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;

public class ImageListingHelper {
    public static final long   IMAGE_LISTING_ID = 1L;
    public static final Date   LISTING_DATE     = Date.from(Instant.now());
    public static final String TITLE            = "title";
    public static final String DESCRIPTION      = "description";
    public static final long   VIEWS            = 0L;
    public static final double PRICE            = 1.0;
    public static final String PATH             = "path";


    public static ImageListingDTO buildImageListingDTO() {
        return ImageListingDTO.builder()
                              .id(IMAGE_LISTING_ID)
                              .listingDate(LISTING_DATE)
                              .title(TITLE)
                              .description(DESCRIPTION)
                              .views(VIEWS)
                              .price(PRICE)
                              .path(PATH)
                              .build();
    }

    public static ImageListing buildImageListing() {
        return ImageListing.builder()
                           .id(IMAGE_LISTING_ID)
                           .listingDate(LISTING_DATE)
                           .title(TITLE)
                           .description(DESCRIPTION)
                           .views(VIEWS)
                           .price(PRICE)
                           .path(PATH)
                           .build();
    }


    public static ImageListing buildBasicImageListing() {
        return ImageListing.builder()
                           .title(TITLE)
                           .description(DESCRIPTION)
                           .views(VIEWS)
                           .price(PRICE)
                           .build();
    }

    public static ImageListingRequestDTO buildImageListingRequestDTO() {
        return ImageListingRequestDTO.builder()
                                     .description(DESCRIPTION)
                                     .price(PRICE)
                                     .title(TITLE)
                                     .build();
    }

    public static BulkImageListingRequestDTO buildBulkImageListingRequestDTO() {
        return BulkImageListingRequestDTO.builder()
                                         .imageListingRequestDTOList(Collections.singletonList(buildImageListingRequestDTO()))
                                         .build();
    }
}
