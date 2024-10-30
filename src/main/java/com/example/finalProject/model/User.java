package com.example.finalProject.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    private String username;
    private String password;
    private String role;

    public UserReturnDto toUserReturnDto() {
        UserReturnDto userReturnDto = new UserReturnDto();
        userReturnDto.setId(this.getUser_id());
        userReturnDto.setUsername(this.getUsername());
        userReturnDto.setRole(this.getRole());
        return userReturnDto;
    }

    public UserUpdateDto toUserUpdateDto() {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setId(this.getUser_id());
        userUpdateDto.setUsername(this.getUsername());
        userUpdateDto.setPassword(this.getPassword());
        userUpdateDto.setRole(this.getRole());
        return userUpdateDto;
    }
}
