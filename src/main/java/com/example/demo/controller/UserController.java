package com.example.demo.controller;

import com.example.demo.dto.ProfileResponse;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(authService.getProfileByEmail(authentication.getName()));
    }

    @PatchMapping("/me")
    public ResponseEntity<ProfileResponse> updateProfile(
            Authentication authentication,
            @RequestBody java.util.Map<String, Object> updates) {
        return ResponseEntity.ok(authService.updateProfile(authentication.getName(), updates));
    }
}
