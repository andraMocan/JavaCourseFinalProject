package com.example.finalProject.repository;

import com.example.finalProject.model.BookCopy;
import com.example.finalProject.model.BookStatus;
import com.example.finalProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {

    List<BookCopy> findAllByOrderByIdDesc();

    List<BookCopy> findAllByRentedBy(User user);

    List<BookCopy> findAllByStatus(BookStatus status);

}
