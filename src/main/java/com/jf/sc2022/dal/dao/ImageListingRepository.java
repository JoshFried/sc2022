package com.jf.sc2022.dal.dao;


import com.jf.sc2022.dal.model.ImageListing;
import com.jf.sc2022.dal.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ImageListingRepository extends JpaRepository<ImageListing, Long> {
    Set<ImageListing> findAllByTags(Tag tag);

    Set<ImageListing> findAllByTitleLike(String title);

    Set<ImageListing> findAllByDescriptionLike(String description);
}
