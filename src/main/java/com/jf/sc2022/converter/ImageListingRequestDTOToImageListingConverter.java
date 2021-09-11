package com.jf.sc2022.converter;

import com.jf.sc2022.dal.model.ImageListing;
import com.jf.sc2022.dto.ImageListingRequestDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;

@Service
public class ImageListingRequestDTOToImageListingConverter implements Converter<ImageListingRequestDTO, ImageListing> {
    @Override
    public ImageListing convert(final ImageListingRequestDTO listingRequestDTO) {
        return ImageListing.builder()
                           .listingDate(Date.from(Instant.now()))
                           .title(listingRequestDTO.getTitle())
                           .description(listingRequestDTO.getDescription())
                           .price(listingRequestDTO.getPrice())
                           .build();
    }
}
