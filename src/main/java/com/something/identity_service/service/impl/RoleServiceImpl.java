package com.something.identity_service.service.impl;

import com.something.identity_service.dto.request.RoleRequest;
import com.something.identity_service.dto.response.RoleResponse;
import com.something.identity_service.entity.Permission;
import com.something.identity_service.entity.Role;
import com.something.identity_service.mapper.RoleMapper;
import com.something.identity_service.repository.PermissionRepository;
import com.something.identity_service.repository.RoleRepository;
import com.something.identity_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;
    @Override
    public RoleResponse create(RoleRequest request) {
        Role role = roleMapper.toRole(request);
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Override
    public List<RoleResponse> findAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    @Override
    public void delete(String name) {
        roleRepository.deleteById(name);
    }
}
