package com.example.demo.service;

import com.example.demo.dto.LearningLanguageDTO;
import com.example.demo.dto.ProfileResponse;
import com.example.demo.entity.Profile;
import com.example.demo.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public List<ProfileResponse> searchProfiles(String nativeLanguage, String learningLanguage) {
        return profileRepository.searchProfiles(nativeLanguage, learningLanguage).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ProfileResponse mapToResponse(Profile profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .username(profile.getUsername())
                .email(profile.getEmail())
                .displayName(profile.getDisplayName())
                .avatarUrl(profile.getAvatarUrl())
                .bio(profile.getBio())
                .latitude(profile.getLatitude())
                .longitude(profile.getLongitude())
                .nativeLanguage(profile.getNativeLanguage())
                .learningLanguages(profile.getLearningLanguages().stream()
                        .map(ll -> new LearningLanguageDTO(ll.getLanguageCode(), ll.getProficiencyLevel()))
                        .collect(Collectors.toList()))
                .roles(profile.getRoles().stream()
                        .map(role -> role.getRole().name())
                        .collect(Collectors.toList()))
                .build();
    }
}
