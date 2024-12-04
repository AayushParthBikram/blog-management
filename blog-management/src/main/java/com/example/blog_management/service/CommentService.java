package com.example.blog_management.service;

import com.example.blog_management.model.Comment;

import java.util.List;

public interface CommentService {
    Comment addComment(Comment comment);
    List<Comment> getCommentsByPostId(Long postId);
}
