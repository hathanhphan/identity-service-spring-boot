package com.something.identity_service.service.impl;

import com.something.identity_service.dto.request.PermissionRequest;
import com.something.identity_service.dto.response.PermissionResponse;
import com.something.identity_service.entity.Permission;
import com.something.identity_service.mapper.PermissionMapper;
import com.something.identity_service.repository.PermissionRepository;
import com.something.identity_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    @Override
    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    @Override
    public List<PermissionResponse> findAll() {
        return permissionRepository.findAll().stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @Override
    public void delete(String name) {
        permissionRepository.deleteById(name);
    }
}
