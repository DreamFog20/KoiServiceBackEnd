package com.example.profile_api.service;

import com.example.profile_api.model.Role;
import com.example.profile_api.model.User;
import com.example.profile_api.repository.RoleRepository;
import com.example.profile_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role getRoleById(Integer roleId) {
        return roleRepository.findById(roleId).orElse(null);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> getRolesByUser(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return List.of(user.getRole()); // Trả về vai trò của người dùng
        }
        return List.of(); // Trả về danh sách rỗng nếu không tìm thấy người dùng
    }

}
