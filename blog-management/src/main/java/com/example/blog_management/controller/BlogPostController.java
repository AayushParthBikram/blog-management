package com.example.blog_management.controller;


import com.example.blog_management.model.BlogPost;
import com.example.blog_management.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogposts")
public class BlogPostController {
    @Autowired
    private BlogPostService blogPostService;

    @PostMapping
    public ResponseEntity<BlogPost> createPost(@RequestBody BlogPost blogPost){
        BlogPost createdPost = blogPostService.createPost(blogPost);
        return  new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    public ResponseEntity<List<BlogPost>> getAllPosts() {
        List<BlogPost> posts = blogPostService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPost> getPostById(@PathVariable Long id) {
        BlogPost post = blogPostService.getPostById(id); // Assuming this method exists in BlogPostService
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPost> updatePost(
            @PathVariable Long id,
            @RequestBody BlogPost blogPost
    ) {
        BlogPost updatedPost = blogPostService.updatePost(id, blogPost); // Assuming this method exists
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        blogPostService.deletePost(id); // Assuming this method exists in BlogPostService
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
