package com.example.kkRecipes.service;

import com.example.kkRecipes.exception.UserNotExistException;
import com.example.kkRecipes.model.User;
import com.example.kkRecipes.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotExistException(username));
    }
}
