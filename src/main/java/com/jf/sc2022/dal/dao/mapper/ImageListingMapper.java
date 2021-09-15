package com.jf.sc2022.dal.dao.mapper;

import com.jf.sc2022.dal.model.ImageListing;
import com.jf.sc2022.dal.service.exceptions.InvalidPriceException;
import com.jf.sc2022.dto.ImageListingDTO;
import com.jf.sc2022.dto.registration.validators.FieldValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageListingMapper {
    public static ImageListing updateImageFields(final ImageListingDTO dto, final ImageListing entity) {
        if (!dto.getTitle().equals(entity.getTitle())) {
            FieldValidator.validateField("Title", dto.getTitle());
            entity.setTitle(dto.getTitle());
        }

        if (!dto.getDescription().equals(entity.getDescription())) {
            entity.setDescription(dto.getDescription());
        }

        if (dto.getPrice() != entity.getPrice()) {
            if (dto.getPrice() < 0) {
                throw new InvalidPriceException("Price must be greater, or equal to 0!");
            }
            entity.setPrice(dto.getPrice());
        }
        return entity;
    }
}
