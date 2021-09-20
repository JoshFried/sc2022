package com.jf.sc2022.controllers;

import com.jf.sc2022.dal.model.Tag;
import com.jf.sc2022.dal.service.ImageListingService;
import com.jf.sc2022.dto.BulkImageListingRequestDTO;
import com.jf.sc2022.dto.ImageListingDTO;
import com.jf.sc2022.dto.ImageListingRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/listings")
public class ImageListingController {
    private final ImageListingService imageListingService;

    @PostMapping("/create")
    public ResponseEntity<ImageListingDTO> addMewListing(@AuthenticationPrincipal final Authentication auth, @ModelAttribute final ImageListingRequestDTO imageListingRequestDTO) {
        return new ResponseEntity<>(imageListingService.handleImageListing(imageListingRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/create-bulk")
    public ResponseEntity<List<ImageListingDTO>> addBulkNewListings(@RequestBody final BulkImageListingRequestDTO bulkImageListingRequestDTO) {
        return new ResponseEntity<>(imageListingService.handleBulkImages(bulkImageListingRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ImageListingDTO> getImageListing(@PathVariable final long id) {
        return new ResponseEntity<>(imageListingService.getImageListing(id), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ImageListingDTO> updateImageListing(@RequestBody final ImageListingDTO imageListingDTO) {
        return new ResponseEntity<>(imageListingService.updateImageListing(imageListingDTO), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ImageListingDTO> deleteImageListing(@PathVariable final long id) {
        return new ResponseEntity<>(imageListingService.deleteImageListing(id), HttpStatus.OK);
    }

    @GetMapping("/searchByTag")
    public ResponseEntity<Set<ImageListingDTO>> searchByTag(@RequestParam final String tag) {
        return new ResponseEntity<>(imageListingService.searchByTag(new Tag(tag)), HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/searchByTags")
    public ResponseEntity<Set<ImageListingDTO>> searchByTags(@RequestParam final Set<String> tags) {
        return new ResponseEntity<>(imageListingService.searchByTags(tags.stream().map(Tag::new).collect(Collectors.toSet())), HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/searchByTitle")
    public ResponseEntity<Set<ImageListingDTO>> searchByTitle(@RequestParam final String title) {
        return new ResponseEntity<>(imageListingService.searchByTitle(title), HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/searchByDescription")
    public ResponseEntity<Set<ImageListingDTO>> searchByDescription(@RequestParam final String description) {
        return new ResponseEntity<>(imageListingService.searchByDescription(description), HttpStatus.ACCEPTED);
    }
}
