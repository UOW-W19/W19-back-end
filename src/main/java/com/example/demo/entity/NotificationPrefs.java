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
public class NotificationPrefs {
    @Builder.Default
    private boolean pushEnabled = true;

    @Builder.Default
    private boolean emailEnabled = false;

    @Builder.Default
    private boolean likeNotifications = true;

    @Builder.Default
    private boolean commentNotifications = true;

    @Builder.Default
    private boolean meetupNotifications = true;
}
