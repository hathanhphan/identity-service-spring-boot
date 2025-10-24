package com.something.identity_service.controller;

import com.something.identity_service.dto.request.UserCreationRequest;
import com.something.identity_service.dto.request.UserUpdateRequest;
import com.something.identity_service.entity.User;
import com.something.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User create(@Valid @RequestBody UserCreationRequest request) {
        return userService.create(request);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable String id) {
        return userService.findById(id);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable String id, @RequestBody UserUpdateRequest request) {
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        userService.deleteById(id);
        return "User has been deleted";
    }
}
