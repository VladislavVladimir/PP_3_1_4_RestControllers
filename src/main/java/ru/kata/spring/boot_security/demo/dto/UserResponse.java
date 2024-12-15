package ru.kata.spring.boot_security.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private long id;
    private String name;
    private String lastName;
    private int age;
    private String email;
    private Set<String> roles;
}


