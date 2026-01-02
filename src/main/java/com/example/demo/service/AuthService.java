package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.Profile;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (profileRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already taken");
        }
        if (profileRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        Profile profile = new Profile();
        profile.setEmail(request.getEmail());
        profile.setUsername(request.getUsername());
        profile.setDisplayName(request.getDisplayName());
        profile.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        Profile savedProfile = profileRepository.save(profile);

        String jwtToken = jwtUtils.generateToken(savedProfile.getEmail());

        return AuthResponse.builder()
                .userId(savedProfile.getId())
                .accessToken(jwtToken)
                .build();
    }
}
