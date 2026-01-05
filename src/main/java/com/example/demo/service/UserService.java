package com.example.demo.service;

import com.example.demo.dto.UserSettingsDTO;
import com.example.demo.entity.Profile;
import com.example.demo.entity.UserBlock;
import com.example.demo.entity.UserSettings;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserSettingsRepository userSettingsRepository;
    private final ProfileRepository profileRepository;
    private final com.example.demo.repository.UserBlockRepository userBlockRepository;

    @Transactional(readOnly = true)
    public UserSettingsDTO getUserSettings(UUID userId) {
        UserSettings settings = userSettingsRepository.findByProfileId(userId)
                .orElse(getSafeDefaultSettings(userId));

        return UserSettingsDTO.builder()
                .notificationPrefs(settings.getNotificationPrefs())
                .privacySettings(settings.getPrivacySettings())
                .theme(settings.getTheme())
                .build();
    }

    @Transactional
    public UserSettingsDTO updateUserSettings(UUID userId, UserSettingsDTO updates) {
        UserSettings settings = userSettingsRepository.findByProfileId(userId)
                .orElse(getSafeDefaultSettings(userId));

        if (updates.getNotificationPrefs() != null) {
            settings.setNotificationPrefs(updates.getNotificationPrefs());
        }
        if (updates.getPrivacySettings() != null) {
            settings.setPrivacySettings(updates.getPrivacySettings());
        }
        if (updates.getTheme() != null) {
            settings.setTheme(updates.getTheme());
        }

        UserSettings saved = userSettingsRepository.save(settings);
        return UserSettingsDTO.builder()
                .notificationPrefs(saved.getNotificationPrefs())
                .privacySettings(saved.getPrivacySettings())
                .theme(saved.getTheme())
                .build();
    }

    private UserSettings getSafeDefaultSettings(UUID userId) {
        // If settings don't exist, create default ones linked to profile
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserSettings newSettings = UserSettings.builder()
                .profile(profile)
                .build();

        return userSettingsRepository.save(newSettings);
    }

    @Transactional
    public void blockUser(UUID blockerId, UUID blockedId) {
        if (blockerId.equals(blockedId)) {
            throw new RuntimeException("Cannot block yourself");
        }

        if (userBlockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId)) {
            throw new RuntimeException("User already blocked");
        }

        Profile blocker = profileRepository.findById(blockerId)
                .orElseThrow(() -> new RuntimeException("Blocker not found"));
        Profile blocked = profileRepository.findById(blockedId)
                .orElseThrow(() -> new RuntimeException("Blocked user not found"));

        UserBlock block = UserBlock.builder()
                .blocker(blocker)
                .blocked(blocked)
                .build();

        userBlockRepository.save(block);

        // TODO: Also unfollow if following?
    }

    @Transactional
    public void unblockUser(UUID blockerId, UUID blockedId) {
        UserBlock block = userBlockRepository.findByBlockerIdAndBlockedId(blockerId, blockedId)
                .orElseThrow(() -> new RuntimeException("Block not found"));

        userBlockRepository.delete(block);
    }
}
