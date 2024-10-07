package com.example.profile_api.config;

import com.example.profile_api.model.User;

import java.util.Map;

public interface JwtGeneratorInterface {
        Map<String, String> generateToken(User user) throws Exception;
}
