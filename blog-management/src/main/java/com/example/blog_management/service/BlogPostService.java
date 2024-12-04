package com.example.blog_management.service;

import com.example.blog_management.model.BlogPost;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BlogPostService {
    BlogPost createPost(BlogPost post);
    List<BlogPost> getAllPosts();

    BlogPost getPostById(Long id);

    BlogPost updatePost(Long id, BlogPost blogPost);

    void deletePost(Long id);
}
