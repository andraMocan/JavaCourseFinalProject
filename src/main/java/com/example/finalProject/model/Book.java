package com.example.finalProject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String language;
    @NotNull
    private int total_copies;
    @Transient
    private BookStatus status;
    @NotNull
    private int page_count;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    // the bookcopy entity owns the relationship (it has the foreign key)
    // "book" value refers to the book field in the book copy entity
    private List<BookCopy> copies = new ArrayList<>();

    public BookStatus getStatus() {
        return this.getAvailableCopies() > 0 ? BookStatus.AVAILABLE : BookStatus.UNAVAILABLE;
    }

    public int getAvailableCopies() {
        return (int) copies.stream()
                .filter(copy -> copy.getStatus() == BookStatus.AVAILABLE)
                .count();
    }

    public BookReturnDto toReturnDto() {
        BookReturnDto bookReturnDto = new BookReturnDto();
        bookReturnDto.setId(this.getId());
        bookReturnDto.setTitle(this.getTitle());
        bookReturnDto.setAuthor(this.getAuthor());
        bookReturnDto.setGenre(this.getGenre());
        bookReturnDto.setLanguage(this.getLanguage());
        bookReturnDto.setStatus(this.getStatus());
        return bookReturnDto;
    }

    public BookUpdateDto toUpdateDto() {
        BookUpdateDto bookUpdateDto = new BookUpdateDto();
        bookUpdateDto.setId(this.getId());
        bookUpdateDto.setTitle(this.getTitle());
        bookUpdateDto.setAuthor(this.getAuthor());
        bookUpdateDto.setIsbn(this.getIsbn());
        bookUpdateDto.setGenre(this.getGenre());
        bookUpdateDto.setPublisher(this.getPublisher());
        bookUpdateDto.setPublication_date(this.getPublication_date());
        bookUpdateDto.setLanguage(this.getLanguage());
        bookUpdateDto.setTotal_copies(this.getTotal_copies());
        bookUpdateDto.setPage_count(this.getPage_count());
        return bookUpdateDto;
    }
}
