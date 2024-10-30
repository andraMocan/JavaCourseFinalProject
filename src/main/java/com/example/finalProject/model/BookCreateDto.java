package com.example.finalProject.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Data
public class BookCreateDto {
    @NotNull
    @NotBlank
    private String title;
    @NotNull
    @NotBlank
    private String author;
    private String isbn;
    private String genre;
    private String publisher;
    private LocalDate publication_date;
    private String language;
    @NotNull
    private int total_copies;
    @NotNull
    private int page_count;
    //status is always available when creating a new book
}
