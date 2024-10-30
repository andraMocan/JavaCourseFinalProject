package com.example.finalProject.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BookCopyReturnDto {
    private Long id;
    private String title;
    private BookStatus status;
    private Long userId;
    private LocalDate rentalStartDate;
    private LocalDate rentalEndDate;
}
