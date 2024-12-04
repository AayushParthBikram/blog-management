package com.example.blog_management.repository;

import com.example.blog_management.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBlogPostId(Long postId);
}