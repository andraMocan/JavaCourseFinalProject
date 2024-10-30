package com.example.finalProject.controller;

import com.example.finalProject.model.*;
import com.example.finalProject.repository.BookCopyRepository;
import com.example.finalProject.repository.BookRepository;
import com.example.finalProject.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/copies")
@RequiredArgsConstructor
public class BookCopyController {

    private final BookCopyService bookCopyService;
    private final BookRepository bookRepository;
    private final RentalService rentalService;
    private final BookCopyRepository bookCopyRepository;

    @GetMapping
    public String getCopies(Model model) {
        List<BookCopyReturnDto> bookCopyReturnDtoList = bookCopyService.findAll();
        model.addAttribute("bookCopyReturnDtoList", bookCopyReturnDtoList);
        return "copies";
    }

    @GetMapping("/create")
    public String createCopyView(@RequestParam("id") Long id, Model model) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + id));
        BookCopyCreateDto bookCopyCreateDto = new BookCopyCreateDto();
        bookCopyCreateDto.setTitle(book.getTitle());
        // the title field will be prepopulated
        model.addAttribute("bookCopyCreateDto", bookCopyCreateDto);
        return "copyCreate";
    }

    @PostMapping("/createCopy")
    public String createCopy(@ModelAttribute @Valid BookCopyCreateDto bookCopyCreateDto) {
        bookCopyService.createCopy(bookCopyCreateDto);
        return "redirect:/copies";
    }

    @GetMapping("/update")
    public String updateCopyView(@RequestParam("id") Long id, Model model) {
        BookCopy bookCopy = bookCopyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book copy not found with id " + id));
        BookCopyUpdateDto bookCopyUpdateDto = bookCopy.toCopyUpdateDto();
        model.addAttribute("bookCopyUpdateDto", bookCopyUpdateDto);
        return "copyUpdate";
    }

    @PostMapping("/updateCopy")
    public String updateCopy(@ModelAttribute @Valid BookCopyUpdateDto bookCopyUpdateDto) {
        bookCopyService.updateCopy(bookCopyUpdateDto);
        return "redirect:/copies";
    }

    @GetMapping("/delete")
    public String deleteCopyById(@RequestParam("id") Long id) {
        bookCopyService.deleteCopyById(id);
        return "redirect:/copies";
    }

    @GetMapping("/rent")
    public String rentView(@RequestParam("id") Long id, Model model) {
        BookCopy bookCopy = bookCopyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book copy not found with id " + id));
        BookCopyRentDto bookCopyRentDto = bookCopy.toCopyRentDto();
        bookCopyRentDto.setRentalStartDate(LocalDate.now());
        bookCopyRentDto.setRentalEndDate(LocalDate.now().plusDays(14));
        model.addAttribute("bookCopyRentDto", bookCopyRentDto);
        return "rent";
    }

    @PostMapping("/rentCopy")
    public String rent(@ModelAttribute @Valid BookCopyRentDto bookCopyRentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUser().getUser_id();
        // getting the user details (id)

        bookCopyRentDto.setUserId(userId);

        rentalService.rent(bookCopyRentDto);

        return "redirect:/copies";
    }

    @GetMapping("/return")
    public String returnView(@RequestParam("id") Long id, Model model) {
        BookCopy bookCopy = bookCopyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book copy not found with id " + id));
        BookCopyRentDto bookCopyRentDto = bookCopy.toCopyRentDto();
        model.addAttribute("bookCopyRentDto", bookCopyRentDto);
        return "returnCopyToLibrary";
    }

    @PostMapping("/returnCopy")
    public String returnCopy(@ModelAttribute @Valid BookCopyRentDto bookCopyRentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUser().getUser_id();
        // get the user id

        BookCopy bookCopy = bookCopyRepository.findById(bookCopyRentDto.getBookCopyId())
                .orElseThrow(() -> new RuntimeException("Book copy not found with id " + bookCopyRentDto.getBookCopyId()));

        if (bookCopy.getRentedBy() != null && bookCopy.getRentedBy().getUser_id().equals(userId)) {
            rentalService.returnCopyToLibrary(bookCopyRentDto);
        } else {
            throw new RuntimeException("You can only return books that you rented.");
        }

        return "redirect:/copies";
    }

}
