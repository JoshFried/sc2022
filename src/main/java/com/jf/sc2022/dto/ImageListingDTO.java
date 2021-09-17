package com.jf.sc2022.dto;


import com.jf.sc2022.dal.model.Tag;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Builder
public class ImageListingDTO {
    private long     id;
    private Date     listingDate;
    private String   title;
    private String   description;
    private long     views;
    private double   price;
    private String   path;
    private Set<Tag> tags;
    private boolean  available;
    private long     userId;
}
