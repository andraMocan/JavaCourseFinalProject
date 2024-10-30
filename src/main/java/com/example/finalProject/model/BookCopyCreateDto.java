package com.example.finalProject.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
public class BookCopyCreateDto {
    @NonNull
    @NotBlank
    private String title;
}
