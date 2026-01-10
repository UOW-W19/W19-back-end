package com.example.demo.controller;

import com.example.demo.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final com.example.demo.service.ProfileService profileService;

    @PostMapping("/{id}/follow")
    public ResponseEntity<Void> followUser(@PathVariable UUID id, Authentication authentication) {
        followService.followUser(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/follow")
    public ResponseEntity<Void> unfollowUser(@PathVariable java.util.UUID id, Authentication authentication) {
        followService.unfollowUser(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/unfollow")
    public ResponseEntity<Void> unfollowUserPost(@PathVariable java.util.UUID id, Authentication authentication) {
        followService.unfollowUser(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<java.util.List<com.example.demo.dto.ProfileResponse>> getFollowers(
            @PathVariable java.util.UUID id) {
        return ResponseEntity.ok(followService.getFollowers(id).stream()
                .map(profileService::mapToResponse)
                .collect(java.util.stream.Collectors.toList()));
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<java.util.List<com.example.demo.dto.ProfileResponse>> getFollowing(
            @PathVariable java.util.UUID id) {
        return ResponseEntity.ok(followService.getFollowing(id).stream()
                .map(profileService::mapToResponse)
                .collect(java.util.stream.Collectors.toList()));
    }
}
