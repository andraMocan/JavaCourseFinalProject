package com.example.finalProject.service;

import com.example.finalProject.model.*;
import com.example.finalProject.repository.BookCopyRepository;
import com.example.finalProject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository mockRepository;

    @Mock
    private BookCopyRepository mockBookCopyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    @Test
    void getAllUsers_ShouldReturnListOfUserReturnDtos() {
        List<User> users = new ArrayList<>();

        User user1 = new User();
        user1.setUser_id(1L);
        user1.setUsername("User1");
        user1.setPassword("1234");
        user1.setRole("USER");

        User user2 = new User();
        user2.setUser_id(2L);
        user2.setUsername("User2");
        user2.setPassword("1235");
        user2.setRole("USER");

        users.add(user1);
        users.add(user2);

        when(mockRepository.findAll()).thenReturn(users);

        List<UserReturnDto> result = userService.getUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("User1", result.get(0).getUsername());
        assertEquals("User2", result.get(1).getUsername());
    }


    @Test
    void createUser_ShouldSaveUnsernameEncodedPasswordAndRole() {

        UserCreateDto userToCreate = new UserCreateDto();
        userToCreate.setUsername("User1");
        userToCreate.setPassword("1234");
        userToCreate.setRole("ADMIN");

        User savedUser = new User();
        savedUser.setUser_id(1L);
        savedUser.setUsername(userToCreate.getUsername());
        savedUser.setPassword("encodedPassword1234");
        savedUser.setRole("ROLE_ADMIN");

        Mockito.when(passwordEncoder.encode("1234")).thenReturn("encodedPassword1234");
        Mockito.when(mockRepository.save(Mockito.any(User.class))).thenReturn(savedUser);

        this.userService.createUser(userToCreate);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(this.mockRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals("User1", capturedUser.getUsername());
        assertEquals("encodedPassword1234", capturedUser.getPassword()); //the password is set already encoded
        assertEquals("ROLE_ADMIN", capturedUser.getRole());
    }

    @Test
    void updateUser_ShouldUpdatePassword() {
        User existingUser = new User();
        existingUser.setUser_id(1L);
        existingUser.setUsername("User1");
        existingUser.setPassword("1234");
        existingUser.setRole("ROLE_USER");

        Mockito.when(passwordEncoder.encode("newpass")).thenReturn("encodednewpass");

        Mockito.when(mockRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setId(1L);
        updateDto.setUsername("User1");
        updateDto.setPassword("newpass");
        updateDto.setRole("USER");

        this.userService.updateUser(updateDto);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(mockRepository).save(userArgumentCaptor.capture());
        User updatedUser = userArgumentCaptor.getValue();

        assertEquals("User1", updatedUser.getUsername());
        assertEquals("encodednewpass", updatedUser.getPassword());
        assertEquals("ROLE_USER", updatedUser.getRole());
    }

    @Test
    void deleteUserById_ShouldDeleteUserAndUpdateBookCopies() {
        User savedUser = new User();
        savedUser.setUser_id(1L);
        savedUser.setUsername("User1");
        savedUser.setPassword("1234");
        savedUser.setRole("ROLE_USER");

        List<BookCopy> copies = new ArrayList<>();

        BookCopy bookCopy1 = new BookCopy();
        bookCopy1.setId(1L);
        bookCopy1.setStatus(BookStatus.BORROWED);
        bookCopy1.setRentedBy(savedUser);
        bookCopy1.setRentalStartDate(LocalDate.now().minusDays(10));
        bookCopy1.setRentalEndDate(LocalDate.now().plusDays(4));

        BookCopy bookCopy2 = new BookCopy();
        bookCopy2.setId(2L);
        bookCopy2.setStatus(BookStatus.BORROWED);
        bookCopy2.setRentedBy(savedUser);
        bookCopy2.setRentalStartDate(LocalDate.now().minusDays(8));
        bookCopy2.setRentalEndDate(LocalDate.now().plusDays(6));

        copies.add(bookCopy1);
        copies.add(bookCopy2);

        Mockito.when(mockRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        Mockito.when(mockBookCopyRepository.findAllByRentedBy(savedUser)).thenReturn(copies);

        this.userService.deleteUserById(1L);

        assertEquals(BookStatus.AVAILABLE, bookCopy1.getStatus());
        assertNull(bookCopy1.getRentedBy());
        assertNull(bookCopy1.getRentalStartDate());
        assertNull(bookCopy1.getRentalEndDate());

        assertEquals(BookStatus.AVAILABLE, bookCopy2.getStatus());
        assertNull(bookCopy2.getRentedBy());
        assertNull(bookCopy2.getRentalStartDate());
        assertNull(bookCopy2.getRentalEndDate());

        Mockito.verify(mockRepository, Mockito.times(1)).deleteById(1L);
    }
}