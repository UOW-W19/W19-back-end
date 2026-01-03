package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.demo.entity.Streak;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private java.util.UUID id;
    private String username;
    private String email;
    private String displayName;
    private String avatarUrl;
    private String bio;
    private Double latitude;
    private Double longitude;
    private Streak streak;
    private java.time.Instant createdAt;
    private List<UserLanguageDTO> languages;
    private List<String> roles;
}
