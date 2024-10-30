package com.example.finalProject.controller;

import com.example.finalProject.model.BookCopyReturnDto;
import com.example.finalProject.service.MyUserDetails;
import com.example.finalProject.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final RentalService rentalService;

    @GetMapping()
    public String getDetails(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUser().getUser_id();

        List<BookCopyReturnDto> bookCopyReturnDtoList = rentalService.getRentedCopiesByUserId(userId);
        double totalPenalties = rentalService.getTotalPenalties(userId);

        model.addAttribute("bookCopyReturnDtoList", bookCopyReturnDtoList);
        model.addAttribute("totalPenalties", totalPenalties);

        return "profile";
    }
}
