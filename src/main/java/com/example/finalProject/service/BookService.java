package com.example.finalProject.service;

import com.example.finalProject.model.*;
import com.example.finalProject.repository.BookCopyRepository;
import com.example.finalProject.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;

    public List<BookReturnDto> getBooks() {
        return bookRepository.findAllByOrderByIdAsc()
                .stream()
                .map(Book::toReturnDto)
                .toList();
    }

    public void createBook(BookCreateDto bookCreateDto) {
        Book book = new Book();
        book.setTitle(bookCreateDto.getTitle());
        book.setAuthor(bookCreateDto.getAuthor());
        book.setIsbn(bookCreateDto.getIsbn());
        book.setGenre(bookCreateDto.getGenre());
        book.setPublisher(bookCreateDto.getPublisher());
        book.setPublication_date(bookCreateDto.getPublication_date());
        book.setLanguage(bookCreateDto.getLanguage());
        book.setTotal_copies(bookCreateDto.getTotal_copies());
        book.setStatus(BookStatus.AVAILABLE);
        book.setPage_count(bookCreateDto.getPage_count());
        bookRepository.save(book);

        for (int i = 0; i < bookCreateDto.getTotal_copies(); i++) {
            BookCopy copy = new BookCopy();
            copy.setBook(book);
            copy.setStatus(BookStatus.AVAILABLE);
            bookCopyRepository.save(copy);
            // creating the required number of book copies, all available when created
        }
    }

    public void updateBook(BookUpdateDto bookUpdateDto) {
        Book book = bookRepository.findById(bookUpdateDto.getId())
                .orElseThrow(() -> new RuntimeException("Book not found with id " + bookUpdateDto.getId()));

        book.setTitle(bookUpdateDto.getTitle());
        book.setAuthor(bookUpdateDto.getAuthor());
        book.setIsbn(bookUpdateDto.getIsbn());
        book.setGenre(bookUpdateDto.getGenre());
        book.setPublisher(bookUpdateDto.getPublisher());
        book.setPublication_date(bookUpdateDto.getPublication_date());
        book.setLanguage(bookUpdateDto.getLanguage());
        book.setPage_count(bookUpdateDto.getPage_count());

        int newTotalCopies = bookUpdateDto.getTotal_copies();
        int currentTotalCopies = book.getTotal_copies();

        if (newTotalCopies > currentTotalCopies) {
            for (int i = 0; i < newTotalCopies - currentTotalCopies; i++) {
                BookCopy bookCopy = new BookCopy();
                bookCopy.setBook(book);
                bookCopy.setStatus(BookStatus.AVAILABLE);
                bookCopyRepository.save(bookCopy);
            }
        }
        book.setTotal_copies(newTotalCopies);
        bookRepository.save(book);
    }

    public void deleteBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            bookRepository.deleteById(id);
        }
    }

}
