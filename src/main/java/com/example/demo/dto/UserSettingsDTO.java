package com.example.demo.dto;

import com.example.demo.entity.NotificationPrefs;
import com.example.demo.entity.PrivacySettings;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSettingsDTO {
    @com.fasterxml.jackson.annotation.JsonProperty("notification_prefs")
    private NotificationPrefs notificationPrefs;

    @com.fasterxml.jackson.annotation.JsonProperty("privacy_settings")
    private PrivacySettings privacySettings;

    private String theme;
}
