package com.example.finalProject.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class BookCopyUpdateDto {
    @NotNull
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private BookStatus status;
}
