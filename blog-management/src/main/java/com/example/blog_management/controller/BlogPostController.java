package com.example.blog_management.controller;

import com.example.blog_management.model.BlogPost;
import com.example.blog_management.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogposts")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    /**
     * Create a new blog post
     * Only accessible by users with ROLE_USER or ROLE_ADMIN
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<BlogPost> createPost(@RequestBody BlogPost blogPost) {
        BlogPost createdPost = blogPostService.createPost(blogPost);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    /**
     * Get all blog posts
     * Only accessible by users with ROLE_USER or ROLE_ADMIN
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<BlogPost>> getAllPosts() {
        List<BlogPost> posts = blogPostService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    /**
     * Get a blog post by its ID
     * Accessible by any authenticated user
     */
    @GetMapping("/{id}")
    @PreAuthorize("authenticated()")  // Any authenticated user can access a specific post
    public ResponseEntity<BlogPost> getPostById(@PathVariable Long id) {
        BlogPost post = blogPostService.getPostById(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    /**
     * Update an existing blog post by its ID
     * Only accessible by users with ROLE_ADMIN
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BlogPost> updatePost(
            @PathVariable Long id,
            @RequestBody BlogPost blogPost
    ) {
        BlogPost updatedPost = blogPostService.updatePost(id, blogPost);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    /**
     * Delete a blog post by its ID
     * Only accessible by users with ROLE_ADMIN
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        blogPostService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
