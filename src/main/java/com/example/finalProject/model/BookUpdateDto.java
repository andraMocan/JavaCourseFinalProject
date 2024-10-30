package com.example.finalProject.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Data
public class BookUpdateDto {
    @NotNull
    private Long id;
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
    private String description;
    private String language;
    @NotNull
    private Integer total_copies;
    @NotNull
    private Integer page_count;

}
