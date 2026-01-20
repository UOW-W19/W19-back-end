package com.example.demo.dto;

import com.example.demo.entity.NotificationPrefs;
import com.example.demo.entity.PrivacySettings;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSettingsDTO {
<<<<<<< HEAD
    @com.fasterxml.jackson.annotation.JsonProperty("notification_prefs")
    private NotificationPrefs notificationPrefs;

    @com.fasterxml.jackson.annotation.JsonProperty("privacy_settings")
=======
    @JsonProperty("notification_prefs")
    private NotificationPrefs notificationPrefs;

    @JsonProperty("privacy_settings")
>>>>>>> feature/users
    private PrivacySettings privacySettings;

    private String theme;
}
