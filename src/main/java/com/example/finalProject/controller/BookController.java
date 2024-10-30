package com.example.finalProject.controller;

import com.example.finalProject.model.Book;
import com.example.finalProject.model.BookCreateDto;
import com.example.finalProject.model.BookReturnDto;
import com.example.finalProject.model.BookUpdateDto;
import com.example.finalProject.repository.BookRepository;
import com.example.finalProject.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;

    @GetMapping
    public String getBooks(Model model) {
        List<BookReturnDto> bookReturnDtoList = bookService.getBooks();
        model.addAttribute("bookReturnDtoList", bookReturnDtoList);
        return "books";
    }

    @GetMapping("/create")
    public String createView(Model model) {
        model.addAttribute("bookCreateDto", new BookCreateDto());
        return "create";
    }

    @PostMapping("/createBook")
    public String createBook(@ModelAttribute @Valid BookCreateDto bookCreateDto) {
        bookService.createBook(bookCreateDto);
        return "redirect:/books";
    }

    @GetMapping("/update")
    public String updateView(@RequestParam("id") Long id, Model model) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + id));
        BookUpdateDto bookUpdateDto = book.toUpdateDto();
        model.addAttribute("bookUpdateDto", bookUpdateDto);
        return "update";
    }

    @PostMapping("/updateBook")
    public String updateBook(@ModelAttribute @Valid BookUpdateDto bookUpdateDto) {
        bookService.updateBook(bookUpdateDto);
        return "redirect:/books";
    }

    @GetMapping("/delete")
    public String deleteBookById(@RequestParam("id") Long id) {
        bookService.deleteBookById(id);
        return "redirect:/books";
    }

}

