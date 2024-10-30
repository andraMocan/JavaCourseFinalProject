package com.example.finalProject.controller;

import com.example.finalProject.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping()
    public String getStatistics(Model model) {
        int totalCopies = statisticsService.getTotalNumberOfCopies();
        int borrowedCopies = statisticsService.getBorrowedNumber();
        int borrowedPercent = statisticsService.getBorrowedPercent();
        int availableCopies = statisticsService.getAvailableNumber();
        int availablePercent = statisticsService.getAvailablePercent();

        model.addAttribute("totalCopies", totalCopies);
        model.addAttribute("borrowedCopies", borrowedCopies);
        model.addAttribute("borrowedPercent", borrowedPercent);
        model.addAttribute("availableCopies", availableCopies);
        model.addAttribute("availablePercent", availablePercent);
        return "statistics";
    }
}
