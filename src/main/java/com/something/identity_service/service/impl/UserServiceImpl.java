package com.something.identity_service.service.impl;

import com.something.identity_service.dto.request.RoleAssignRequest;
import com.something.identity_service.dto.request.UserCreationRequest;
import com.something.identity_service.dto.request.UserUpdateRequest;
import com.something.identity_service.dto.response.UserResponse;
import com.something.identity_service.entity.Role;
import com.something.identity_service.entity.User;
import com.something.identity_service.mapper.UserMapper;
import com.something.identity_service.repository.RoleRepository;
import com.something.identity_service.repository.UserRepository;
import com.something.identity_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse create(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public UserResponse findById(String id) {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }

    @Override
    public UserResponse update(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponse getMyInfo() {
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        log.info("Username is {}", name);
        User user = userRepository.findByUsername(name).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse assignRole(RoleAssignRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        List<Role> roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }
}
