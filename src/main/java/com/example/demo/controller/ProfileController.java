package com.example.demo.controller;

import com.example.demo.dto.ProfileResponse;
import com.example.demo.service.AuthService;
import com.example.demo.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final AuthService authService;
    private final ProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return ResponseEntity.ok(authService.getProfileByEmail(email));
    }

    @GetMapping
    public ResponseEntity<java.util.List<ProfileResponse>> searchProfiles(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String nativeLanguage,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String learningLang) {
        return ResponseEntity.ok(profileService.searchProfiles(nativeLanguage, learningLang));
    }
}
