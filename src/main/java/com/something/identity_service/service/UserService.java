package com.something.identity_service.service;

import com.something.identity_service.dto.request.UserCreationRequest;
import com.something.identity_service.dto.request.UserUpdateRequest;
import com.something.identity_service.entity.User;

import java.util.List;

public interface UserService {
    User create(UserCreationRequest request);

    List<User> findAll();

    User findById(String id);

    User update(String id, UserUpdateRequest request);

    void deleteById(String id);
}
