package com.jf.sc2022.dal.dao;


import com.jf.sc2022.dal.model.ImageListing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageListingRepository extends JpaRepository<ImageListing, Long> {
}
