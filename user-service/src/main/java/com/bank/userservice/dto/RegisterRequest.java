package com.bank.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RegisterRequest{

    @NotBlank(message="Full name is Required")
    private String fullName;

    @NotBlank(message="Email is Required")
    @Email(message="Email Must be Valid")
    private String email;

    @NotBlank(message="Username is Required")
    @Size(min=4,max=20,message="Username must be 4-20 characters")
    private String username;

    @NotBlank(message="Password is Required")
    @Size(min=8,message="Password must be at least 8 characters")
    private String password;

    private String phone;
}
