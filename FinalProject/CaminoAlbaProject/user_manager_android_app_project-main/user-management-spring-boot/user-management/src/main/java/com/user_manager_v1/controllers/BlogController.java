package com.user_manager_v1.controllers;

import com.user_manager_v1.models.Blog;
import com.user_manager_v1.models.Publication;
import com.user_manager_v1.repository.BlogRepository;
import com.user_manager_v1.services.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private PublicationService publicationService;
    @Autowired
    private BlogRepository blogRepository;

    @GetMapping("/all")
    public List<Blog> getBlogs() {
        return blogRepository.findAll();
    }

    @PostMapping("/add/{blogId}/publications")
    public ResponseEntity<Publication> addPublication(@PathVariable Long blogId, @RequestBody Publication publication) {
        publicationService.addPublication(blogId, publication);
        return new ResponseEntity<>(publication, HttpStatus.CREATED);
    }
}
