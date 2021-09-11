package com.jf.sc2022.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
public class ImageListingRequestDTO {
    private String        title;
    private String        description;
    private double        price;
    private MultipartFile multipartFile;
}
