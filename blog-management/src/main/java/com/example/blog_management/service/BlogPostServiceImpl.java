package com.example.blog_management.service;

import com.example.blog_management.model.BlogPost;
import com.example.blog_management.repository.BlogRepository;
import com.example.blog_management.exception.BlogPostNotFoundException; // Custom Exception
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogPostServiceImpl implements BlogPostService {

    private final BlogRepository blogRepository;
    private static final Logger logger = LoggerFactory.getLogger(BlogPostServiceImpl.class);

    public BlogPostServiceImpl(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Override
    public BlogPost createPost(BlogPost post) {
        logger.info("Creating new blog post with title: {}", post.getBlog_title());
        return blogRepository.save(post);
    }

    @Override
    public List<BlogPost> getAllPosts() {
        logger.info("Fetching all blog posts.");
        return blogRepository.findAll();
    }

    @Override
    public BlogPost getPostById(Long id) {
        logger.info("Fetching blog post with ID: {}", id);
        return blogRepository.findById(id)
                .orElseThrow(() -> new BlogPostNotFoundException("Blog post with ID " + id + " not found."));
    }

    @Override
    public BlogPost updatePost(Long id, BlogPost blogPost) {
        logger.info("Updating blog post with ID: {}", id);
        BlogPost existingPost = blogRepository.findById(id)
                .orElseThrow(() -> new BlogPostNotFoundException("Blog post with ID " + id + " not found."));

        // Update fields
        existingPost.setBlog_title(blogPost.getBlog_title());
        existingPost.setContent(blogPost.getContent());
        existingPost.setThumbnailImage(blogPost.getThumbnailImage());

        return blogRepository.save(existingPost);
    }

    @Override
    public void deletePost(Long id) {
        logger.info("Deleting blog post with ID: {}", id);
        BlogPost existingPost = blogRepository.findById(id)
                .orElseThrow(() -> new BlogPostNotFoundException("Blog post with ID " + id + " not found."));

        // Delete the blog post
        blogRepository.delete(existingPost);
        logger.info("Blog post with ID {} deleted successfully.", id);
    }
}
