package com.example.finalProject.service;

import com.example.finalProject.model.*;
import com.example.finalProject.repository.BookCopyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private BookCopyRepository mockBookCopyRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    private List<BookCopy> copies;

    @BeforeEach
    public void beforeEach() {
        copies = new ArrayList<>();

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

        BookCopy copy3 = new BookCopy();
        copy3.setId(3L);
        copy3.setBook(book);
        copy3.setStatus(BookStatus.AVAILABLE);
        copy3.setRentedBy(user);

        BookCopy copy4 = new BookCopy();
        copy4.setId(4L);
        copy4.setBook(book);
        copy4.setStatus(BookStatus.AVAILABLE);
        copy4.setRentedBy(user);

        copies.add(copy1);
        copies.add(copy2);
        copies.add(copy3);
        copies.add(copy4);
    }

    @Test
    void getTotalNumberOfCopies_ShouldReturnTotalCount() {
        when(mockBookCopyRepository.findAll()).thenReturn(copies);

        int result = statisticsService.getTotalNumberOfCopies();

        assertEquals(4, result);
    }

    @Test
    void getBorrowedNumber_ShouldReturnCountOfBorrowedCopies() {
        when(mockBookCopyRepository.findAllByStatus(BookStatus.BORROWED)).thenReturn(copies
                .stream()
                .filter(copy -> copy.getStatus() == BookStatus.BORROWED)
                .toList()
        );

        int result = statisticsService.getBorrowedNumber();

        assertEquals(1, result);
    }

    @Test
    void getBorrowedPercent_ShouldReturnPercentageOfBorrowedCopies() {
        when(mockBookCopyRepository.findAll()).thenReturn(copies);

        when(mockBookCopyRepository.findAllByStatus(BookStatus.BORROWED)).thenReturn(copies
                .stream()
                .filter(copy -> copy.getStatus() == BookStatus.BORROWED)
                .toList()
        );

        int result = statisticsService.getBorrowedPercent();

        assertEquals(25, result);


    }

    @Test
    void getAvailableNumber_ShouldReturnCountOfAvailableCopies() {
        when(mockBookCopyRepository.findAllByStatus(BookStatus.AVAILABLE)).thenReturn(copies
                .stream()
                .filter(copy -> copy.getStatus() == BookStatus.AVAILABLE)
                .toList()
        );

        int result = statisticsService.getAvailableNumber();

        assertEquals(3, result);
    }

    @Test
    void getAvailablePercent_ShouldReturnPercentageOfAvailableCopies() {
        when(mockBookCopyRepository.findAll()).thenReturn(copies);

        when(mockBookCopyRepository.findAllByStatus(BookStatus.AVAILABLE)).thenReturn(copies
                .stream()
                .filter(copy -> copy.getStatus() == BookStatus.AVAILABLE)
                .toList()
        );

        int result = statisticsService.getAvailablePercent();

        assertEquals(75, result);

    }
}