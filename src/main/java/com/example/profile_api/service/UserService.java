package com.example.profile_api.service;

import com.example.profile_api.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(int id);
    List<User> getALlUser();
    User updateUser(int id, User userDetails);
    User registerUser( User userDetails);
    public void saveUser(User user);
    public User getUserByEmailAndPassword(String name, String password) ;
}

