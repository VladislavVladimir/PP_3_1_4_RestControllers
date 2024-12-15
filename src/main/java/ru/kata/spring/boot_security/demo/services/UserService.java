package ru.kata.spring.boot_security.demo.services;

import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.demo.dto.UserRequest;
import ru.kata.spring.boot_security.demo.dto.UserResponse;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    List<UserResponse> listUsers();
    UserResponse getUser(long id);
    UserResponse getUserByEmail(String email);
    String deleteUser(long id);
    void saveUser(User user);
    String updateUser(long id, UserRequest user, BindingResult bindingResult);
    String createUser(UserRequest user,  BindingResult bindingResult);
}
