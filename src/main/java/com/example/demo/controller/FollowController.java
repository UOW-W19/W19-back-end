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

    @PostMapping("/{id}/follow")
    public ResponseEntity<Void> followUser(@PathVariable UUID id, Authentication authentication) {
        followService.followUser(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/follow")
    public ResponseEntity<Void> unfollowUser(@PathVariable UUID id, Authentication authentication) {
        followService.unfollowUser(id, authentication.getName());
        return ResponseEntity.ok().build();
    }
}
