package com.example.blog_management.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String content;

    @ManyToOne
    @JoinColumn(name = "blog_post_id")
    private BlogPost blogPost;


}
