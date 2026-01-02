package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.ProfileResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.Profile;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
        Profile user = profileRepository.findByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken = jwtUtils.generateToken(user.getEmail());
        return AuthResponse.builder()
                .userId(user.getId())
                .accessToken(jwtToken)
                .build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (profileRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already taken");
        }

        String username = request.getUsername();
        if (username == null || username.isBlank()) {
            username = request.getEmail().split("@")[0];
        }

        if (profileRepository.existsByUsername(username)) {
            username = username + System.currentTimeMillis() % 1000;
        }

        Profile profile = new Profile();
        profile.setEmail(request.getEmail());
        profile.setUsername(username);
        profile.setDisplayName(request.getDisplayName());
        profile.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        profile.setNativeLanguage(request.getNativeLanguage());
        profile.setLearningLanguages(request.getLearningLanguages());

        profile.addRole(com.example.demo.enums.AppRole.USER);

        Profile savedProfile = profileRepository.save(profile);

        String jwtToken = jwtUtils.generateToken(savedProfile.getEmail());

        return AuthResponse.builder()
                .userId(savedProfile.getId())
                .accessToken(jwtToken)
                .build();
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfileByEmail(String email) {
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return ProfileResponse.builder()
                .id(profile.getId())
                .username(profile.getUsername())
                .email(profile.getEmail())
                .displayName(profile.getDisplayName())
                .avatarUrl(profile.getAvatarUrl())
                .bio(profile.getBio())
                .latitude(profile.getLatitude())
                .longitude(profile.getLongitude())
                .nativeLanguage(profile.getNativeLanguage())
                .learningLanguages(profile.getLearningLanguages())
                .roles(profile.getRoles().stream()
                        .map(role -> role.getRole().name())
                        .collect(Collectors.toList()))
                .build();
    }
}
