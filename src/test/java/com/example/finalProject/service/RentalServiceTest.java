package com.example.finalProject.service;

import com.example.finalProject.model.*;
import com.example.finalProject.repository.BookCopyRepository;
import com.example.finalProject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {
    @Mock
    private BookCopyRepository mockBookCopyRepository;

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private RentalService rentalService;

    @Test
    void calculatePenaltiesPerBook_ShouldCalculateCorrectPenaltyAmount() {

        BookCopyRentDto copyRent1 = new BookCopyRentDto();
        copyRent1.setUserId(3L);
        copyRent1.setRentalStartDate(LocalDate.now().minusDays(20));
        copyRent1.setRentalEndDate(LocalDate.now().minusDays(6));
        copyRent1.setBookCopyId(2L);
        copyRent1.setPenalties(0);

        rentalService.setPenaltyPerDay(3.0);

        rentalService.calculatePenaltiesPerBook(copyRent1);

        assertEquals(18.0, copyRent1.getPenalties());
    }

    @Test
    void getRentedCopiesByUserId_ShouldReturnListOfRentedCopiesByUser() {
        User user = new User();
        user.setUser_id(3L);

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Title 1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Title 2");

        Book book3 = new Book();
        book3.setId(3L);
        book3.setTitle("Title 3");

        BookCopy bookCopy1 = new BookCopy();
        bookCopy1.setId(1L);
        bookCopy1.setStatus(BookStatus.BORROWED);
        bookCopy1.setBook(book1);
        bookCopy1.setRentedBy(user);

        BookCopy bookCopy2 = new BookCopy();
        bookCopy2.setId(2L);
        bookCopy2.setStatus(BookStatus.BORROWED);
        bookCopy2.setBook(book2);
        bookCopy2.setRentedBy(user);

        BookCopy bookCopy3 = new BookCopy();
        bookCopy3.setId(3L);
        bookCopy3.setStatus(BookStatus.BORROWED);
        bookCopy3.setBook(book3);
        bookCopy3.setRentedBy(user);

        List<BookCopy> list = List.of(bookCopy1, bookCopy2, bookCopy3);

        when(mockUserRepository.findById(3L)).thenReturn(Optional.of(user));
        when(mockBookCopyRepository.findAllByRentedBy(user)).thenReturn(list);

        List<BookCopyReturnDto> returnedList = rentalService.getRentedCopiesByUserId(3L);

        assertEquals(3, returnedList.size());
        assertEquals(1L, returnedList.get(0).getId());
        assertEquals(2L, returnedList.get(1).getId());
        assertEquals(3L, returnedList.get(2).getId());

    }

    @Test
    void getTotalPenalties_ShouldCalculateTotalPenaltiesForUser() {
        User user = new User();
        user.setUser_id(3L);

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Title 1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Title 2");

        Book book3 = new Book();
        book3.setId(3L);
        book3.setTitle("Title 3");

        BookCopy bookCopy1 = new BookCopy();
        bookCopy1.setId(1L);
        bookCopy1.setStatus(BookStatus.BORROWED);
        bookCopy1.setBook(book1);
        bookCopy1.setRentedBy(user);
        bookCopy1.setRentalStartDate(LocalDate.now().minusDays(20));
        bookCopy1.setRentalEndDate(LocalDate.now().minusDays(6));

        BookCopy bookCopy2 = new BookCopy();
        bookCopy2.setId(2L);
        bookCopy2.setStatus(BookStatus.BORROWED);
        bookCopy2.setBook(book2);
        bookCopy2.setRentedBy(user);
        bookCopy2.setRentalStartDate(LocalDate.now().minusDays(15));
        bookCopy2.setRentalEndDate(LocalDate.now().minusDays(1));

        BookCopy bookCopy3 = new BookCopy();
        bookCopy3.setId(3L);
        bookCopy3.setStatus(BookStatus.BORROWED);
        bookCopy3.setBook(book3);
        bookCopy3.setRentedBy(user);
        bookCopy3.setRentalStartDate(LocalDate.now().minusDays(17));
        bookCopy3.setRentalEndDate(LocalDate.now().minusDays(3));

        List<BookCopy> list1 = List.of(bookCopy1, bookCopy2, bookCopy3);

        when(mockUserRepository.findById(3L)).thenReturn(Optional.of(user));
        when(mockBookCopyRepository.findAllByRentedBy(user)).thenReturn(list1);

        rentalService.setPenaltyPerDay(3.0);

        assertEquals(30.0, rentalService.getTotalPenalties(3L));
    }

    @Test
    void rent_ShouldChangeStatusToBorrowedAndSetRentalDates() {
        BookCopy bookCopy = new BookCopy();
        bookCopy.setId(2L);
        bookCopy.setStatus(BookStatus.AVAILABLE);

        User user = new User();
        user.setUser_id(3L);

        BookCopyRentDto copyRent1 = new BookCopyRentDto();
        copyRent1.setUserId(3L);
        copyRent1.setRentalStartDate(LocalDate.now());
        copyRent1.setRentalEndDate(LocalDate.now().plusDays(14));
        copyRent1.setBookCopyId(2L);

        when(mockUserRepository.findById(3L)).thenReturn(Optional.of(user));
        when(mockBookCopyRepository.findById(2L)).thenReturn(Optional.of(bookCopy));
        when(mockBookCopyRepository.save(any(BookCopy.class))).thenReturn(bookCopy);

        rentalService.rent(copyRent1);

        assertEquals(BookStatus.BORROWED, bookCopy.getStatus());
        assertEquals(user, bookCopy.getRentedBy());
        assertEquals(LocalDate.now(), bookCopy.getRentalStartDate());
        assertEquals(LocalDate.now().plusDays(14), bookCopy.getRentalEndDate());
    }

    @Test
    void returnCopyToLibrary_ShouldResetStatusAndClearRentalDetails() {
        BookCopy bookCopy = new BookCopy();
        bookCopy.setId(2L);
        bookCopy.setStatus(BookStatus.BORROWED);
        bookCopy.setRentedBy(new User());
        bookCopy.setRentalStartDate(LocalDate.now().minusDays(10));
        bookCopy.setRentalEndDate(LocalDate.now().plusDays(4));

        BookCopyRentDto copyRent1 = new BookCopyRentDto();
        copyRent1.setUserId(null);
        copyRent1.setRentalStartDate(null);
        copyRent1.setRentalEndDate(null);
        copyRent1.setBookCopyId(2L);

        when(mockBookCopyRepository.findById(2L)).thenReturn(Optional.of(bookCopy));
        when(mockBookCopyRepository.save(any(BookCopy.class))).thenReturn(bookCopy);

        rentalService.returnCopyToLibrary(copyRent1);

        assertEquals(BookStatus.AVAILABLE, bookCopy.getStatus());
        assertNull(bookCopy.getRentedBy());
        assertNull(bookCopy.getRentalStartDate());
        assertNull(bookCopy.getRentalEndDate());
    }
}