package com.jf.sc2022.dto;


import com.jf.sc2022.dal.model.Tag;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Builder
@Data
public class ImageListingRequestDTO {
    private String        title;
    private String        description;
    private double        price;
    private boolean       available;
    private Set<Tag>      tags;
    private MultipartFile multipartFile;
}
