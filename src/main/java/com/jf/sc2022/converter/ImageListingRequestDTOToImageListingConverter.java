package com.jf.sc2022.converter;

import com.jf.sc2022.dal.model.ImageListing;
import com.jf.sc2022.dal.model.Tag;
import com.jf.sc2022.dto.ImageListingRequestDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ImageListingRequestDTOToImageListingConverter implements Converter<ImageListingRequestDTO, ImageListing> {
    @Override
    public ImageListing convert(final ImageListingRequestDTO listingRequestDTO) {
        return ImageListing.builder()
                           .title(listingRequestDTO.getTitle())
                           .tags(buildTagSet(listingRequestDTO.getTags()))
                           .available(listingRequestDTO.isAvailable())
                           .description(listingRequestDTO.getDescription())
                           .price(listingRequestDTO.getPrice())
                           .build();
    }

    private Set<Tag> buildTagSet(final Set<String> tags) {
        return tags.stream().map(Tag::new).collect(Collectors.toSet());
    }
}
