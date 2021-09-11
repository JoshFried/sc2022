package com.jf.sc2022.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class BulkImageListingRequestDTO {
    private List<ImageListingRequestDTO> imageListingRequestDTOList;
}
