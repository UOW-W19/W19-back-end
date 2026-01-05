package com.example.demo.controller;

import com.example.demo.dto.UserSettingsDTO;
import com.example.demo.service.UserService;
import com.example.demo.entity.Profile;
import com.example.demo.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

        private final UserService userService;
        private final ProfileRepository profileRepository;
        private final com.example.demo.service.ProfileService profileService;

        @GetMapping("/me")
        public ResponseEntity<com.example.demo.dto.ProfileResponse> getCurrentUser(Authentication authentication) {
                Profile profile = profileRepository.findByEmail(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("User not found"));
                return ResponseEntity.ok(profileService.mapToResponse(profile));
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
