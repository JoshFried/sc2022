package com.jf.sc2022.dal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "image_listings")
public class ImageListing {
    @Id
    @GeneratedValue
    private long   id;
    private Date   listingDate;
    private String title;
    private String description;
    private long   views;
    private double averageRating;

    @OneToMany(cascade = CascadeType.ALL)
    private Map<Long, Rating> ratings;
    private double            price;

    @Column(unique = true)
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
