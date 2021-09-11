package com.jf.sc2022.converter;


import com.jf.sc2022.dal.model.ImageListing;
import com.jf.sc2022.dto.ImageListingDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class ImageListingToImageListingDTOConverter implements Converter<ImageListing, ImageListingDTO> {
    @Override
    public ImageListingDTO convert(final ImageListing imageListing) {
        return ImageListingDTO.builder()
                              .id(imageListing.getId())
                              .description(imageListing.getDescription())
                              .listingDate(imageListing.getListingDate())
                              .path(imageListing.getPath())
                              .price(imageListing.getPrice())
                              .title(imageListing.getTitle())
                              .views(imageListing.getViews())
                              .build();
    }
}
