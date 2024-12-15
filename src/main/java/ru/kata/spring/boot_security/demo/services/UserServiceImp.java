package ru.kata.spring.boot_security.demo.services;

import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.demo.dto.UserRequest;
import ru.kata.spring.boot_security.demo.dto.UserResponse;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImp implements UserService, UserDetailsService {
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponse> listUsers() {
        return userDao.findAll().stream().map(this::mapToResponse).collect(Collectors.toList()) ;
    }

    @Override
    public UserResponse getUser(long id) {
        return mapToResponse(userDao.getById(id));
    }

    @Override
    public String deleteUser(long id) {
        userDao.deleteById(id);
        return "Success";
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        return mapToResponse(userDao.findByEmail(email));
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getLastName(), user.getAge(), user.getEmail(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
    }

    @Override
    public void saveUser(User user) {
        if ((user.getId() == 0) || (user.getId() > 0 && !user.getPassword().isEmpty())) { //если нужно изменить пароль
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else { //если нужно оставить старый пароль
            User updateUser = userDao.getById(user.getId());
            user.setPassword(updateUser.getPassword());
        }
        userDao.save(user);
    }

    @Override
    public String updateUser(long id, UserRequest user, BindingResult bindingResult) {
        if (problemValid(id, user, bindingResult)) {
            return bindingResultToString(bindingResult);
        }
        User UserUpdate = new User(id, user.getName(), user.getLastName(), user.getAge(), user.getEmail(),user.getPassword(),
                 user.getRoles().stream().map(roleDao::findByName).collect(Collectors.toSet()));
        saveUser(UserUpdate);
        return "Success";
    }

    @Override
    public String createUser(UserRequest user, BindingResult bindingResult) {
        return updateUser(0L, user, bindingResult);
    }

    private boolean problemValid(long id, UserRequest user, BindingResult bindingResult) {
        user.setPassword(user.getPassword().trim());
        if ((!user.getPassword().isEmpty() && user.getPassword().length()<4) ||   //если пароль 1-3 символа
                (user.getPassword().isEmpty() && id==0)) {  //или 0 и user только создается
            bindingResult.rejectValue("password", "error.user", "Пароль должен содержать минимум 4 символа");
        }
        User userUniqueEmail = userDao.findByEmail(user.getEmail());    // ищем существующих юзеров с текущей почтой
        if (userUniqueEmail != null && userUniqueEmail.getId() != id) {  // если это другой юзер, то у нас проблемы
            bindingResult.rejectValue("email", "error.user", "Email уже занят");
        }

        return bindingResult.hasErrors();
    }

    private String bindingResultToString(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().map(error -> error.getField() + ": " +
                error.getDefaultMessage()).collect(Collectors.joining(", "));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s not found", email));
        }
        Hibernate.initialize(user.getRoles()); //загружаем роли
        return user;
    }
}
