package com.example.demo.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
    private String content;
    @com.fasterxml.jackson.annotation.JsonProperty("original_language")
    private String originalLanguage;

    @com.fasterxml.jackson.annotation.JsonProperty("image_url")
    private String imageUrl;
    private Double latitude;
    private Double longitude;
}
