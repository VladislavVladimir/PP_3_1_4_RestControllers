package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserResponse;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/user/info")
public class UserInfoAuth {
    private final UserService userService;

    @Autowired
    public UserInfoAuth(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public UserResponse getUserInfo(Principal principal) {
        return userService.getUserByEmail(principal.getName());
    }
}