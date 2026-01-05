package com.example.demo.controller;

import com.example.demo.dto.ProfileResponse;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.dto.UserSettingsDTO;
import com.example.demo.service.UserService;
import com.example.demo.entity.Profile;
import com.example.demo.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

        private final UserService userService;
        private final ProfileRepository profileRepository;
        private final com.example.demo.service.ProfileService profileService;

        @GetMapping("/me")
        public ResponseEntity<ProfileResponse> getCurrentUser(Authentication authentication) {
                Profile profile = profileRepository.findByEmail(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("User not found"));
                return ResponseEntity.ok(profileService.mapToResponse(profile));
        }

        @GetMapping("/{userId}")
        public ResponseEntity<ProfileResponse> getPublicProfile(@PathVariable UUID userId) {
                Profile profile = profileRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));
                return ResponseEntity.ok(profileService.mapToResponse(profile));
        }

        @PatchMapping("/me")
        public ResponseEntity<ProfileResponse> updateProfile(
                        Authentication authentication,
                        @RequestBody UpdateProfileRequest request) {
                Profile profile = profileRepository.findByEmail(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                if (request.getDisplayName() != null) {
                        profile.setDisplayName(request.getDisplayName());
                }
                if (request.getBio() != null) {
                        profile.setBio(request.getBio());
                }
                if (request.getAvatarUrl() != null) {
                        profile.setAvatarUrl(request.getAvatarUrl());
                }
                if (request.getLatitude() != null) {
                        profile.setLatitude(request.getLatitude());
                }
                if (request.getLongitude() != null) {
                        profile.setLongitude(request.getLongitude());
                }

                Profile updated = profileRepository.save(profile);
                return ResponseEntity.ok(profileService.mapToResponse(updated));
        }

        @GetMapping("/me/settings")
        public ResponseEntity<UserSettingsDTO> getMySettings(Authentication authentication) {
                Profile profile = profileRepository.findByEmail(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("User not found"));
                return ResponseEntity.ok(userService.getUserSettings(profile.getId()));
        }

        @PatchMapping("/me/settings")
        public ResponseEntity<UserSettingsDTO> updateMySettings(
                        Authentication authentication,
                        @RequestBody UserSettingsDTO settings) {
                Profile profile = profileRepository.findByEmail(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("User not found"));
                return ResponseEntity.ok(userService.updateUserSettings(profile.getId(), settings));
        }

        @PostMapping("/{userId}/block")
        public ResponseEntity<Void> blockUser(
                        Authentication authentication,
                        @PathVariable java.util.UUID userId) {
                Profile blocker = profileRepository.findByEmail(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                userService.blockUser(blocker.getId(), userId);
                return ResponseEntity.ok().build();
        }

        @DeleteMapping("/{userId}/block")
        public ResponseEntity<Void> unblockUser(
                        Authentication authentication,
                        @PathVariable java.util.UUID userId) {
                Profile blocker = profileRepository.findByEmail(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                userService.unblockUser(blocker.getId(), userId);
                return ResponseEntity.ok().build();
        }
}
