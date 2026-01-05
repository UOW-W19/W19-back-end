package com.example.demo.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivacySettings {
    @Builder.Default
    private boolean showLocation = true;

    @Builder.Default
    private String allowMessages = "everyone"; // everyone, following, none
}
