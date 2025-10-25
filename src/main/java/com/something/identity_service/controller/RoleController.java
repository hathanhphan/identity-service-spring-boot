package com.something.identity_service.controller;

import com.something.identity_service.dto.request.RoleRequest;
import com.something.identity_service.dto.response.ApiResponse;
import com.something.identity_service.dto.response.RoleResponse;
import com.something.identity_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> create(@RequestBody RoleRequest request) {
        return ResponseEntity.ok(ApiResponse.success(roleService.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(roleService.findAll()));
    }

    @DeleteMapping("{roleName}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("roleName") String roleName) {
        roleService.delete(roleName);
        return ResponseEntity.ok(ApiResponse.success("Role deleted successfully"));
    }
}
