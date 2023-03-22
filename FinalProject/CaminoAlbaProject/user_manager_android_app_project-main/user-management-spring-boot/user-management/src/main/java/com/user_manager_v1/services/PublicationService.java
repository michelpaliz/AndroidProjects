package com.user_manager_v1.services;

import com.user_manager_v1.models.Blog;
import com.user_manager_v1.models.Publication;
import com.user_manager_v1.repository.BlogRepository;
import com.user_manager_v1.repository.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicationService {
    @Autowired
    private PublicationRepository publicationRepository;
    @Autowired
    private BlogRepository blogRepository;

    public void addPublication(Long blogId, Publication publication) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new IllegalArgumentException("Invalid blog id"));
        publication.setBlog(blog);
        publicationRepository.save(publication);
//        Publication publication = new Publication(null, "My Publication", "This is my publication", "2023-03-22", "https://example.com/photo.jpg", null);
//        publicationService.addPublication(1L, publication);
    }
}
