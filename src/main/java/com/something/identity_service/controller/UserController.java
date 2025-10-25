package com.something.identity_service.controller;

import com.something.identity_service.dto.request.RoleAssignRequest;
import com.something.identity_service.dto.request.UserCreationRequest;
import com.something.identity_service.dto.request.UserUpdateRequest;
import com.something.identity_service.dto.response.ApiResponse;
import com.something.identity_service.dto.response.UserResponse;
import com.something.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(@Valid @RequestBody UserCreationRequest request) {
        return ResponseEntity.ok(ApiResponse.success("User created successfully", userService.create(request)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAll() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(authority -> {
            log.info("Authorities: {}", authority.getAuthority());
        });
        return ResponseEntity.ok(ApiResponse.success(userService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> findById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(userService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(@PathVariable String id, @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", userService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        userService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo() {
        return ResponseEntity.ok(ApiResponse.success(userService.getMyInfo()));
    }

    @PostMapping("/assign-role")
    public ResponseEntity<ApiResponse<UserResponse>> assignRole(@RequestBody RoleAssignRequest request) {
        return ResponseEntity.ok(ApiResponse.success("User assigned role successfully", userService.assignRole(request)));
    }
}
