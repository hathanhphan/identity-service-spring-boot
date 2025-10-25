package com.something.identity_service.controller;

import com.something.identity_service.dto.request.PermissionRequest;
import com.something.identity_service.dto.response.ApiResponse;
import com.something.identity_service.dto.response.PermissionResponse;
import com.something.identity_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public ResponseEntity<ApiResponse<PermissionResponse>> create(@RequestBody PermissionRequest request) {
        return ResponseEntity.ok(ApiResponse.success(permissionService.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(permissionService.findAll()));
    }

    @DeleteMapping("{permissionName}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("permissionName") String permissionName) {
        permissionService.delete(permissionName);
        return ResponseEntity.ok(ApiResponse.success("Permission deleted successfully"));
    }
}
