package com.example.blog_management.service;

import com.example.blog_management.model.BlogPost;
import com.example.blog_management.repository.BlogRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BlogPostServiceImpl implements BlogPostService {
    private final BlogRepository blogRepository;

    public BlogPostServiceImpl(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Override
    public BlogPost createPost(BlogPost post) {
        return blogRepository.save(post);
    }

    @Override
    public List<BlogPost> getAllPosts(){
        return blogRepository.findAll();
    }

    @Override
    public BlogPost getPostById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog post with ID " + id + " not found."));
    }

    @Override
    public BlogPost updatePost(Long id, BlogPost blogPost) {
        BlogPost existingPost = blogRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Blog post with ID" +id + "not found."));

        existingPost.setBlog_title(blogPost.getBlog_title());
        existingPost.setContent(blogPost.getContent());
        existingPost.setThumbnailImage(blogPost.getThumbnailImage());

        return blogRepository.save(existingPost);

    }

    @Override
    public void deletePost(Long id) {

        BlogPost existingPost = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog post with ID " + id + " not found."));

        // Delete the blog post
        blogRepository.delete(existingPost);

    }


}
