package com.something.identity_service.service;

import com.something.identity_service.dto.request.PermissionRequest;
import com.something.identity_service.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse create(PermissionRequest request);
    List<PermissionResponse> findAll();
    void delete(String name);
}
