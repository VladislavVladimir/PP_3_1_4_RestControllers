package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.services.RoleService;

import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminsController {
    private final RoleService roleService;

    @Autowired
    public AdminsController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping()
    public String mainPage(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("roles", ((Authentication)principal).getAuthorities().
                stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        model.addAttribute("availableRoles", roleService.listRoles());
        return "adminPanel";
    }
}