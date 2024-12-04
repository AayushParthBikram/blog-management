package com.example.blog_management.repository;

import com.example.blog_management.model.BlogPost;
import com.example.blog_management.model.Comment;
import com.example.blog_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BlogRepository extends JpaRepository<BlogPost, Long>{}


