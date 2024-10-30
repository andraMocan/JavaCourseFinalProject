package com.example.finalProject.service;

import com.example.finalProject.model.*;
import com.example.finalProject.repository.BookCopyRepository;
import com.example.finalProject.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookCopyServiceTest {

    @Mock
    private BookCopyRepository mockBookCopyRepository;

    @Mock
    private BookRepository mockBookRepository;

    @InjectMocks
    private BookCopyService bookCopyService;

    @Test
    void findAll_ShouldReturnAllBookCopies() {
        List<BookCopy> copies = new ArrayList<>();

        Book book = new Book();
        book.setTitle("Test Book");

        User user = new User();
        user.setUser_id(3L);

        BookCopy copy1 = new BookCopy();
        copy1.setId(1L);
        copy1.setBook(book);
        copy1.setStatus(BookStatus.AVAILABLE);
        copy1.setRentedBy(user);

        BookCopy copy2 = new BookCopy();
        copy2.setId(2L);
        copy2.setBook(book);
        copy2.setStatus(BookStatus.BORROWED);
        copy2.setRentedBy(user);

        copies.add(copy1);
        copies.add(copy2);

        when(mockBookCopyRepository.findAllByOrderByIdDesc()).thenReturn(copies);

        List<BookCopyReturnDto> result = bookCopyService.findAll();

        assertEquals(2, result.size());

        assertEquals("Test Book", result.get(0).getTitle());
        assertEquals(BookStatus.AVAILABLE, result.get(0).getStatus());
        assertEquals(3L, result.get(0).getUserId());

        assertEquals("Test Book", result.get(1).getTitle());
        assertEquals(BookStatus.BORROWED, result.get(1).getStatus());
        assertEquals(3L, result.get(1).getUserId());
    }

    @Test
    void createCopy_ShouldIncrementTotalCopiesForBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setTotal_copies(1);

        BookCopyCreateDto bookCopyCreateDto = new BookCopyCreateDto();
        bookCopyCreateDto.setTitle(book.getTitle());
        // the input data

        when(mockBookRepository.findByTitle("Test Book")).thenReturn(Optional.of(book));

        BookCopy savedCopy = new BookCopy();
        savedCopy.setStatus(BookStatus.AVAILABLE);
        //the book copy that will be created

        when(mockBookCopyRepository.save(any(BookCopy.class))).thenReturn(savedCopy);

        bookCopyService.createCopy(bookCopyCreateDto);

        assertEquals(2, book.getTotal_copies());
    }

    @Test
    void updateCopy_ShouldUpdateBookCopyStatus() {
        Book book = new Book();
        book.setId(5L);

        BookCopy existingCopy = new BookCopy();
        existingCopy.setId(1L);
        existingCopy.setBook(book);
        existingCopy.setStatus(BookStatus.BORROWED);

        when(mockBookCopyRepository.findById(1L)).thenReturn(Optional.of(existingCopy));

        BookCopyUpdateDto updateDto = new BookCopyUpdateDto();
        updateDto.setId(1L);
        updateDto.setStatus(BookStatus.AVAILABLE);

        this.bookCopyService.updateCopy(updateDto);

        ArgumentCaptor<BookCopy> bookCaptor = ArgumentCaptor.forClass(BookCopy.class);
        Mockito.verify(mockBookCopyRepository).save(bookCaptor.capture());
        BookCopy updatedCopy = bookCaptor.getValue();
        // in order to catch the bookCopy that was saved and inspect it

        assertEquals(BookStatus.AVAILABLE, updatedCopy.getStatus());
        assertEquals(1L, updatedCopy.getId());
    }

    @Test
    void deleteCopyById_ShouldDecrementTotalCopiesForBook() {

        Book book = new Book();
        book.setId(5L);
        book.setTitle("Test Title");
        book.setTotal_copies(2);

        BookCopy savedCopy = new BookCopy();
        savedCopy.setId(1L);
        savedCopy.setBook(book);
        savedCopy.setStatus(BookStatus.AVAILABLE);

        Mockito.when(mockBookCopyRepository.findById(1L)).thenReturn(Optional.of(savedCopy));

        this.bookCopyService.deleteCopyById(1L);

        assertEquals(1, book.getTotal_copies());
    }
}