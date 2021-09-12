package com.jf.sc2022.converter;

import com.jf.sc2022.dal.model.ImageListing;
import com.jf.sc2022.dto.ImageListingRequestDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class ImageListingRequestDTOToImageListingConverter implements Converter<ImageListingRequestDTO, ImageListing> {
    @Override
    public ImageListing convert(final ImageListingRequestDTO listingRequestDTO) {
        return ImageListing.builder()
                           .title(listingRequestDTO.getTitle())
                           .tags(listingRequestDTO.getTags())
                           .available(listingRequestDTO.isAvailable())
                           .description(listingRequestDTO.getDescription())
                           .price(listingRequestDTO.getPrice())
                           .build();
    }
}
