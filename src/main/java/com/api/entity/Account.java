package com.api.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerNumber;

    private String customerName;
    private String customerMobile;

    @NotBlank(message = "Email is required field")
    @Email(message = "Invalid email format")
    private String customerEmail;

    private String address1;
    private String address2;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;
}
