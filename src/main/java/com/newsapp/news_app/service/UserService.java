package com.newsapp.news_app.service;

import com.newsapp.news_app.entity.User;
import com.newsapp.news_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String username, String email, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username is already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already in use");
        }

        User user = new User(username, email, passwordEncoder.encode(password));
        Set<String> defaultPreferences = new HashSet<>();
        defaultPreferences.add("general");
        defaultPreferences.add("technology");
        user.setPreferences(defaultPreferences);

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUserPreferences(Set<String> preferences) {
        User user = getCurrentUser();
        user.setPreferences(preferences);
        return userRepository.save(user);
    }

    public Set<String> getUserPreferences() {
        User user = getCurrentUser();
        return user.getPreferences();
    }

}
