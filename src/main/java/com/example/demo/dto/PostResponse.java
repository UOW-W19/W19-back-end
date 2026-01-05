package com.example.demo.dto;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private UUID id;
    private AuthorDto author;
    private String content;
    private String translation; // Placeholder for future use
    private String originalLanguage;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private String location; // Human readable location
    private String distance; // Calculated distance
    private PostReactionsSummary reactions;
    private String userReaction;
    private Instant createdAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorDto {
        private UUID id;
        private String username;
        private String displayName;
        private String avatarUrl;
        private String language;
        private String flagEmoji;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostReactionsSummary {
        private long likes;
        private long comments;
    }
}
