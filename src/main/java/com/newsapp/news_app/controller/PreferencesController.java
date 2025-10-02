package com.newsapp.news_app.controller;

import com.newsapp.news_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/preferences")
public class PreferencesController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Set<String>> getPreferences() {
        Set<String> preferences = userService.getUserPreferences();
        return ResponseEntity.ok(preferences);
    }

    @PutMapping
    public ResponseEntity<?> updatePreferences(@RequestBody Set<String> newPreferences) {
        if(newPreferences == null || newPreferences.isEmpty()) {
            return ResponseEntity.badRequest().body("Preferences cannot be empty");
        }

        userService.updateUserPreferences(newPreferences);
        return ResponseEntity.ok("Preferences updated successfully");
    }

}
