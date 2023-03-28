package com.user_manager_v1.controllers;

import com.user_manager_v1.models.Blog;
import com.user_manager_v1.models.Publication;
import com.user_manager_v1.models.dto.BlogDTO;
import com.user_manager_v1.repository.BlogRepository;
import com.user_manager_v1.services.PublicationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private PublicationService publicationService;
    @Autowired
    private BlogRepository blogRepository;

//    @GetMapping("/all")
//    public List<Blog> getBlogs() {
//        return blogRepository.findAll();
//    }

    @GetMapping("/all")
    public ResponseEntity<List<BlogDTO>> getBlogs() {
        ModelMapper modelMapper = new ModelMapper();
        List<Blog> blogs = blogRepository.findAll();
        List<BlogDTO> blogDTOs = new ArrayList<>();
        for (Blog blog : blogs) {
            BlogDTO blogDTO = modelMapper.map(blog, BlogDTO.class);
            blogDTOs.add(blogDTO);
        }
        return new ResponseEntity<>(blogDTOs, HttpStatus.OK);
    }


    @PostMapping("/add/{blogId}/publications")
    public ResponseEntity<Publication> addPublication(@PathVariable Long blogId, @RequestBody Publication publication) {
        publicationService.addPublication(blogId, publication);
        return new ResponseEntity<>(publication, HttpStatus.CREATED);
    }

    @PutMapping("/blog/{id}")
    public ResponseEntity<Blog> updateBlog(@PathVariable int id, @RequestBody Blog updatedBlog) {
//        When you call the findById() method of a Spring Data JPA repository, it returns an Optional instead of the actual entity.
//        This is because the requested entity may not exist in the database, in which case the Optional will be empty.
//        Using Optional allows you to handle this scenario without having to explicitly check for null values, which can help make your code more concise and readable.
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isPresent()) {
            Blog blogToUpdate = optionalBlog.get();
            blogToUpdate.setDescription(updatedBlog.getDescription());
            blogToUpdate.setEnableInfo(updatedBlog.isEnableInfo());
            blogToUpdate.setKmlRunned(updatedBlog.getKmlRunned());
            blogToUpdate.setPoints(updatedBlog.getPoints());
            blogToUpdate.setFollowers(updatedBlog.getFollowers());
            blogToUpdate.setFollowing(updatedBlog.getFollowing());
            blogToUpdate.setPublications(updatedBlog.getPublications());
            Blog updated = blogRepository.save(blogToUpdate);
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
