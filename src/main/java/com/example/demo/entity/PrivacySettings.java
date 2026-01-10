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
    @com.fasterxml.jackson.annotation.JsonProperty("show_location")
    private boolean showLocation = true;

    @Builder.Default
    @com.fasterxml.jackson.annotation.JsonProperty("allow_messages")
    private String allowMessages = "everyone"; // everyone, following, none
}
