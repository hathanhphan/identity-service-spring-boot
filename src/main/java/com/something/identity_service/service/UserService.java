package com.something.identity_service.service;

import com.something.identity_service.dto.request.RoleAssignRequest;
import com.something.identity_service.dto.request.UserCreationRequest;
import com.something.identity_service.dto.request.UserUpdateRequest;
import com.something.identity_service.dto.response.UserResponse;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserResponse create(UserCreationRequest request);

    List<UserResponse> findAll();

    UserResponse findById(String id);

    UserResponse update(String id, UserUpdateRequest request);

    void deleteById(String id);

    UserResponse getMyInfo();

    UserResponse assignRole(RoleAssignRequest request);
}
