package com.example.finalProject.service;

import com.example.finalProject.model.*;
import com.example.finalProject.repository.BookCopyRepository;
import com.example.finalProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BookCopyRepository bookCopyRepository;

    public List<UserReturnDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(User::toUserReturnDto)
                .toList();
    }

    public void createUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setRole("ROLE_" + userCreateDto.getRole().toUpperCase());
        userRepository.save(user);
    }

    public void updateUser(UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userUpdateDto.getId())
                .orElseThrow(() -> new RuntimeException("Book not found with id " + userUpdateDto.getId()));

        user.setUsername(userUpdateDto.getUsername());
        user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        user.setRole("ROLE_" + userUpdateDto.getRole().toUpperCase());
        userRepository.save(user);
    }


    public void deleteUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<BookCopy> copies = bookCopyRepository.findAllByRentedBy(user);
            for (BookCopy copy : copies) {
                copy.setStatus(BookStatus.AVAILABLE);
                copy.setRentedBy(null);
                copy.setRentalStartDate(null);
                copy.setRentalEndDate(null);
            }
            bookCopyRepository.saveAll(copies);
            userRepository.deleteById(id);
        }

    }
}
