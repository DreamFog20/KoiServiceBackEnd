package com.example.profile_api.service;

import com.example.profile_api.model.Role;
import com.example.profile_api.model.User;
import com.example.profile_api.repository.RoleRepository;
import com.example.profile_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(int id, User updatedUser) {

        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setAddress(updatedUser.getAddress());
            return userRepository.save(user);
        }).orElseGet(() -> {
            updatedUser.setUserID(id);
            return userRepository.save(updatedUser);
        });
    }

    public User registerUser(User user) {
        List<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (!existingUser.isEmpty()) {
            throw new RuntimeException("Email đã được đăng ký");
        }


        Role userRole = null;
        try {
            userRole = roleRepository.findByName("Customer")
                    .orElseThrow(() -> new Exception("Không tìm thấy vai trò người dùng"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        user.setRole(userRole);
        return userRepository.save(user);


    }



    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
    @Override
    public User getUserByEmailAndPassword(String name, String password)  {
        User user = userRepository.findByEmailAndPassword(name, password);
        if(user == null){
            try {
                throw new Exception("Invalid id and password");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return user;
    }
    public List<User> getALlUser(){
        return userRepository.findAll();
    }
}

