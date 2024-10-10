package com.example.profile_api.service;

import com.example.profile_api.model.Role;

import java.util.List;

public interface RoleService {
    Role saveRole(Role role);
    Role getRoleById(Integer roleId);
    List<Role> getAllRoles();
    List<Role> getRolesByUser(Integer userId);
}