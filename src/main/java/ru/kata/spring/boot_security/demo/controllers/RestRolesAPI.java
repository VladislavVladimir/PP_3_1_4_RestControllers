package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.services.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RestRolesAPI {
    private final RoleService roleService;

    public RestRolesAPI(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public String[] getAllRoles() {
        return roleService.listRoles().stream().map(Role::getName).toArray(String[]::new);
    }
}