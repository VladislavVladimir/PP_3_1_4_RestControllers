package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserRequest;
import ru.kata.spring.boot_security.demo.dto.UserResponse;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class RestUsersAPI {
    private final UserService userService;

    public RestUsersAPI(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.listUsers();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public String addUser(@Valid @RequestBody UserRequest user, BindingResult bindingResult) {
        return userService.createUser(user, bindingResult);
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest user, BindingResult bindingResult) {
        return userService.updateUser(id, user, bindingResult);
    }

    @DeleteMapping("/{id}")
    public String deleteUser (@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
