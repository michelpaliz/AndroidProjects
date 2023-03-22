package com.user_manager_v1.controllers;

import com.user_manager_v1.models.Publication;
import com.user_manager_v1.repository.PublicationRepository;
import com.user_manager_v1.services.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blogs")
public class PublicationController {
    @Autowired
    private PublicationService publicationService;

    @Autowired
    private PublicationRepository publicationRepository;


    @PostMapping("/add/{blogId}/publications")
    public ResponseEntity<Publication> addPublication(@PathVariable Long blogId, @RequestBody Publication publication) {
        publicationService.addPublication(blogId, publication);
        return new ResponseEntity<>(publication, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Publication> updatePublication(@RequestBody Publication publication) {
        Publication existingPublication = publicationRepository.findById(publication.getId()).orElse(null);
        if (existingPublication == null) {
            return ResponseEntity.notFound().build();
        }
        existingPublication.setTitle(publication.getTitle());
        existingPublication.setDescription(publication.getDescription());
        existingPublication.setDatePublished(publication.getDatePublished());
        existingPublication.setPhotos(publication.getPhotos());
        Publication updatedPublication = publicationRepository.save(existingPublication);
        return ResponseEntity.ok(updatedPublication);
    }

}