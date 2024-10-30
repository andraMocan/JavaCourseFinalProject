package com.example.finalProject.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCopyRentDto {

    private Long userId;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate rentalStartDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate rentalEndDate;
    private Long bookCopyId;
    private double penalties;

}
