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
public class BookCopyService {

    private final BookCopyRepository bookCopyRepository;
    private final BookRepository bookRepository;

    public List<BookCopyReturnDto> findAll() {
        return bookCopyRepository.findAllByOrderByIdDesc()
                .stream()
                .map(BookCopy::toCopyReturnDto)
                .toList();
    }

    public void createCopy(BookCopyCreateDto bookCopyCreateDto) {
        BookCopy bookCopy = new BookCopy();
        Optional<Book> optionalBook = bookRepository.findByTitle(bookCopyCreateDto.getTitle());
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            bookCopy.setBook(book);
            bookCopy.setStatus(BookStatus.AVAILABLE);
            bookCopyRepository.save(bookCopy);
            book.setTotal_copies(book.getTotal_copies() + 1);
            bookRepository.save(book);
        } else {
            throw new RuntimeException("Book with title " + bookCopyCreateDto.getTitle() + " not found.");
        }
    }


    public void updateCopy(BookCopyUpdateDto bookCopyUpdateDto) {
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyUpdateDto.getId())
                .orElseThrow(() -> new RuntimeException("Book not found with id " + bookCopyUpdateDto.getId()));

        bookCopy.setStatus(bookCopyUpdateDto.getStatus());
        bookCopyRepository.save(bookCopy);
    }

    public void deleteCopyById(Long id) {
        Optional<BookCopy> optionalBookCopy = bookCopyRepository.findById(id);

        if (optionalBookCopy.isPresent()) {
            BookCopy bookCopy = optionalBookCopy.get();
            Book book = bookCopy.getBook();
            book.setTotal_copies(book.getTotal_copies() - 1);
            bookCopyRepository.deleteById(id);

            if (book.getTotal_copies() == 0) {
                bookRepository.delete(book);
            } else {
                bookRepository.save(book);
            }
        }
    }

}
