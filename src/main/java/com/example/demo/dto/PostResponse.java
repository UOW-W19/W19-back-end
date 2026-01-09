package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private UUID id;
    private String content;
    private String originalLanguage;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private String location;
    private String distance;
    private LocalDateTime createdAt;
    private String status;
    private AuthorDTO author;
    private ReactionSummaryDTO reactions;
    private String userReaction;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorDTO {
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
    public static class ReactionSummaryDTO {
        private int likes;
        private int comments;
    }
}
