package com.jf.sc2022.dto;


import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ImageListingDTO {
    private long   id;
    private Date   listingDate;
    private String title;
    private String description;
    private long   views;
    private double price;
    private String path;
}
