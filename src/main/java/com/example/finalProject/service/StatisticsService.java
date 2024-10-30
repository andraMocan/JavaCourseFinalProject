package com.example.finalProject.service;

import com.example.finalProject.model.BookStatus;
import com.example.finalProject.repository.BookCopyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final BookCopyRepository bookCopyRepository;

    public int getTotalNumberOfCopies() {
        return bookCopyRepository.findAll().size();
    }

    public int getBorrowedNumber() {
        return bookCopyRepository.findAllByStatus(BookStatus.BORROWED).size();
    }

    public int getBorrowedPercent() {
        int borrowed = this.getBorrowedNumber();
        int total = this.getTotalNumberOfCopies();
        return (total == 0) ? 0 : borrowed * 100 / total;
    }

    public int getAvailableNumber() {
        return bookCopyRepository.findAllByStatus(BookStatus.AVAILABLE).size();
    }

    public int getAvailablePercent() {
        int available = this.getAvailableNumber();
        int total = this.getTotalNumberOfCopies();
        return (total == 0) ? 0 : available * 100 / total;
    }
}
