package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private java.util.UUID userId;
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
}
