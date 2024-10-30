package com.example.finalProject.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
public class UserUpdateDto {
    private Long id;
    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    @Size(min = 4, message = "Password must be at least 4 characters long")
    private String password;
    private String role;
}
