package com.example.demo.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
    private String content;
    private String originalLanguage;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
}
