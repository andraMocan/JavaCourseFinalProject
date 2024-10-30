package com.example.finalProject.model;

import lombok.Data;

@Data
public class BookReturnDto {
    private Long id;
    private String title;
    private String author;
    private String genre;
    private String language;
    private BookStatus status;
}
