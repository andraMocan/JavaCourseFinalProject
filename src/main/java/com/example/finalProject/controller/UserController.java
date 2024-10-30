package com.example.finalProject.controller;

import com.example.finalProject.model.*;
import com.example.finalProject.repository.UserRepository;
import com.example.finalProject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    public String getUsers(Model model) {
        List<UserReturnDto> userReturnDtoList = userService.getUsers();
        model.addAttribute("userReturnDtoList", userReturnDtoList);
        return "users";
    }

    @GetMapping("/create")
    public String createUserView(Model model) {
        model.addAttribute("userCreateDto", new UserCreateDto());
        return "userCreate";
    }

    @PostMapping("/createUser")
    public String createUser(@ModelAttribute @Valid UserCreateDto userCreateDto) {
        userService.createUser(userCreateDto);
        return "redirect:/users";
    }

    @GetMapping("/update")
    public String updateUserView(@RequestParam("id") Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + id));
        UserUpdateDto userUpdateDto = user.toUserUpdateDto();
        model.addAttribute("userUpdateDto", userUpdateDto);
        return "userUpdate";
    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute @Valid UserUpdateDto userUpdateDto) {
        userService.updateUser(userUpdateDto);
        return "redirect:/users";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/users";
    }
}
