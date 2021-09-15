package com.jf.sc2022.controllers;

import com.jf.sc2022.dal.service.ImageListingService;
import com.jf.sc2022.dto.BulkImageListingRequestDTO;
import com.jf.sc2022.dto.ImageListingDTO;
import com.jf.sc2022.dto.ImageListingRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageListingController {
    private final ImageListingService imageListingService;

    @PostMapping
    public ResponseEntity<ImageListingDTO> addMewListing(@RequestBody final ImageListingRequestDTO imageListingRequestDTO) {
        return new ResponseEntity<>(imageListingService.handleImageListing(imageListingRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/temp")
    public ResponseEntity<List<ImageListingDTO>> addBulkNewListings(@RequestBody final BulkImageListingRequestDTO bulkImageListingRequestDTO) {
        return new ResponseEntity<>(imageListingService.handleBulkImages(bulkImageListingRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ImageListingDTO> getImageListing(@PathVariable final long id) {
        return new ResponseEntity<>(imageListingService.getImageListing(id), HttpStatus.OK);
    }

    @PutMapping("/temp")
    public ResponseEntity<ImageListingDTO> updateImageListing(@RequestBody final ImageListingDTO imageListingDTO) {
        return new ResponseEntity<>(imageListingService.updateImageListing(imageListingDTO), HttpStatus.ACCEPTED);
    }
}
