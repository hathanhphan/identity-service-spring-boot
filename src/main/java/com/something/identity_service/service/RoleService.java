package com.something.identity_service.service;

import com.something.identity_service.dto.request.PermissionRequest;
import com.something.identity_service.dto.request.RoleRequest;
import com.something.identity_service.dto.response.PermissionResponse;
import com.something.identity_service.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse create(RoleRequest request);
    List<RoleResponse> findAll();
    void delete(String name);
}
