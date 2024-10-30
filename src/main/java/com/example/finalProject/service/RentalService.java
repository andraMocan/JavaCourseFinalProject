package com.example.finalProject.service;

import com.example.finalProject.model.*;
import com.example.finalProject.repository.BookCopyRepository;
import com.example.finalProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;

    @Value("${penalty.per.day}")
    @Setter
    private double penaltyPerDay;

    public void calculatePenaltiesPerBook(BookCopyRentDto bookCopyRentDto) {
        long days = ChronoUnit.DAYS.between(bookCopyRentDto.getRentalEndDate(), LocalDate.now());

        if (days > 0) {
            bookCopyRentDto.setPenalties(days * penaltyPerDay);
        } else {
            bookCopyRentDto.setPenalties(0);
        }
    }

    public List<BookCopyReturnDto> getRentedCopiesByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return bookCopyRepository.findAllByRentedBy(user.get())
                    .stream()
                    .map(BookCopy::toCopyReturnDto)
                    .toList();
        } else {
            throw new RuntimeException("User not found.");
        }
    }
    // used for the profile page

    public double getTotalPenalties(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        int sum = 0;
        if (user.isPresent()) {
            List<BookCopyRentDto> bookCopyRentDtoList = bookCopyRepository.findAllByRentedBy(user.get())
                    .stream()
                    .map(BookCopy::toCopyRentDto)
                    .toList();
            for (BookCopyRentDto bookCopyRentDto : bookCopyRentDtoList) {
                if (bookCopyRentDto.getRentalEndDate() != null
                        && LocalDate.now().isAfter(bookCopyRentDto.getRentalEndDate())) {
                    calculatePenaltiesPerBook(bookCopyRentDto);
                }
                sum += bookCopyRentDto.getPenalties();
            }
        } else {
            throw new RuntimeException("User not found.");
        }
        return sum;
    }

    public void rent(BookCopyRentDto bookCopyRentDto) {
        Optional<User> optionalUser = userRepository.findById(bookCopyRentDto.getUserId());
        Optional<BookCopy> optionalBookCopy = bookCopyRepository.findById(bookCopyRentDto.getBookCopyId());
        if (optionalUser.isPresent() && optionalBookCopy.isPresent()) {
            User user = optionalUser.get();
            BookCopy bookCopy = optionalBookCopy.get();
            if (bookCopy.getStatus() == BookStatus.AVAILABLE) {
                bookCopy.setStatus(BookStatus.BORROWED);
                bookCopy.setRentedBy(user);
                bookCopy.setRentalStartDate(LocalDate.now());
                bookCopy.setRentalEndDate(LocalDate.now().plusDays(14));
                bookCopyRepository.save(bookCopy);
            } else {
                throw new RuntimeException("Book copy is not available for rent.");
            }
        } else {
            throw new RuntimeException("User or BookCopy not found.");
        }

    }

    public void returnCopyToLibrary(BookCopyRentDto bookCopyRentDto) {

        Optional<BookCopy> optionalBookCopy = bookCopyRepository.findById(bookCopyRentDto.getBookCopyId());
        if (optionalBookCopy.isEmpty()) {
            throw new RuntimeException("BookCopy not found.");
        }
        BookCopy bookCopy = optionalBookCopy.get();

        if (bookCopy.getStatus() == BookStatus.BORROWED) {
            bookCopy.setStatus(BookStatus.AVAILABLE);
            bookCopy.setRentedBy(null);
            bookCopy.setRentalStartDate(null);
            bookCopy.setRentalEndDate(null);
            bookCopyRepository.save(bookCopy);
        } else {
            throw new RuntimeException("Book copy is not rented.");
        }

    }
}
