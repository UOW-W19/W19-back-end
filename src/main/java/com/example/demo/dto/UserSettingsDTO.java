package com.example.demo.dto;

import com.example.demo.entity.NotificationPrefs;
import com.example.demo.entity.PrivacySettings;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSettingsDTO {
    private NotificationPrefs notificationPrefs;
    private PrivacySettings privacySettings;
    private String theme;
}
