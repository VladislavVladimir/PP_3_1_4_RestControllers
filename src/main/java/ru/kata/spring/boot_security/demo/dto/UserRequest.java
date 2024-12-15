package ru.kata.spring.boot_security.demo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserRequest {
    @Size(min = 2, max = 30, message = "Имя должно содержать от 2 до 30 символов")
    private String name;

    @Size(min = 2, max = 30, message = "Фамилия должна содержать от 2 до 30 символов")
    private String lastName;

    @Min(value = 0, message = "Возраст не может быть отрицательным")
    @Max(value = 150, message = "Возраст не может превышать 150 лет")
    private int age;

    @Email(message = "Введите корректный email")
    private String email;

    private String password;

    private Set<String> roles = new HashSet<>();;
}
