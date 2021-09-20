package com.jf.sc2022.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
public class ImageListingRequestDTO {
    private String        title;
    private String        description;
    private double        price;
    private boolean       available;
    private Set<String>   tags;
    private MultipartFile multipartFile;
}
