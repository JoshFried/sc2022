package com.jf.sc2022.dal.dao;


import com.jf.sc2022.dal.model.ImageListing;
import com.jf.sc2022.dal.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageListingRepository extends JpaRepository<ImageListing, Long> {
    List<ImageListing> findAllByTags(Tag tag);

    List<ImageListing> findAllByTagsContains(List<Tag> tagSet);
}
