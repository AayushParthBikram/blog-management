package com.example.blog_management.service;

import com.example.blog_management.model.Comment;
import com.example.blog_management.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByBlogPostId(postId);
    }
}
