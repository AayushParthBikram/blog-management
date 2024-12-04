package com.example.blog_management.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String blog_title;
    private String content;
    private String thumbnailImage;

    @OneToMany(mappedBy = "blogPost", cascade = CascadeType.ALL)

    private List<Comment> comments;

}
