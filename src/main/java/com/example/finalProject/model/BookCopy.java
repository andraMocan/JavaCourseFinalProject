package com.example.finalProject.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "book_copies")
public class BookCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_copy_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @ManyToOne
    @JoinColumn(name = "rented_by", referencedColumnName = "user_id")
    private User rentedBy;

    private LocalDate rentalStartDate;

    private LocalDate rentalEndDate;


    public BookCopyReturnDto toCopyReturnDto() {
        BookCopyReturnDto bookCopyReturnDto = new BookCopyReturnDto();
        bookCopyReturnDto.setId(this.getId());
        bookCopyReturnDto.setTitle(this.getBook().getTitle());
        bookCopyReturnDto.setStatus(this.getStatus());
        bookCopyReturnDto.setUserId(this.getRentedBy() != null ? this.getRentedBy().getUser_id() : null);
        bookCopyReturnDto.setRentalStartDate(this.getRentalStartDate());
        bookCopyReturnDto.setRentalEndDate(this.getRentalEndDate());
        return bookCopyReturnDto;
    }

    public BookCopyUpdateDto toCopyUpdateDto() {
        BookCopyUpdateDto bookCopyUpdateDto = new BookCopyUpdateDto();
        bookCopyUpdateDto.setId(this.getId());
        bookCopyUpdateDto.setTitle(this.getBook().getTitle());
        bookCopyUpdateDto.setStatus(this.getStatus());
        return bookCopyUpdateDto;
    }

    public BookCopyRentDto toCopyRentDto() {
        BookCopyRentDto bookCopyRentDto = new BookCopyRentDto();
        bookCopyRentDto.setUserId(this.getRentedBy() != null ? this.getRentedBy().getUser_id() : null);
        bookCopyRentDto.setRentalStartDate(this.getRentalStartDate());
        bookCopyRentDto.setRentalEndDate(this.getRentalEndDate());
        bookCopyRentDto.setBookCopyId(this.getId());
        return bookCopyRentDto;
    }
}
