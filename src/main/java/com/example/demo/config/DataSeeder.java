package com.example.demo.config;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.example.demo.entity.Language;
import com.example.demo.entity.Profile;
import com.example.demo.entity.UserLanguage;
import com.example.demo.repository.LanguageRepository;
import com.example.demo.repository.UserLanguageRepository;
import java.util.List;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final AuthService authService;
    private final ProfileRepository profileRepository;
    private final LanguageRepository languageRepository;
    private final UserLanguageRepository userLanguageRepository;

    @Override
    public void run(String... args) {
        if (profileRepository.count() == 0) {
            log.info("Seeding demo data...");

            // Seed Languages
            Language en = Language.builder().code("en").name("English").nativeName("English").flagEmoji("🇬🇧").build();
            Language es = Language.builder().code("es").name("Spanish").nativeName("Español").flagEmoji("🇪🇸").build();
            Language vi = Language.builder().code("vi").name("Vietnamese").nativeName("Tiếng Việt").flagEmoji("🇻🇳")
                    .build();
            languageRepository.saveAll(List.of(en, es, vi));

            RegisterRequest demoUserRequest = new RegisterRequest();
            demoUserRequest.setEmail("demo@locale.app");
            demoUserRequest.setPassword("demo123");
            demoUserRequest.setDisplayName("Demo User");
            demoUserRequest.setUsername("demouser");

            authService.register(demoUserRequest);

            // Add languages to demo user
            Profile demoProfile = profileRepository.findByEmail("demo@locale.app").orElseThrow();

            userLanguageRepository.save(UserLanguage.builder()
                    .profile(demoProfile)
                    .language(en)
                    .proficiency(com.example.demo.enums.ProficiencyLevel.NATIVE)
                    .isLearning(false)
                    .build());

            userLanguageRepository.save(UserLanguage.builder()
                    .profile(demoProfile)
                    .language(es)
                    .proficiency(com.example.demo.enums.ProficiencyLevel.B2)
                    .isLearning(true)
                    .build());

            log.info("Demo data seeded successfully! You can now login with demo@locale.app / demo123");
        }
    }
}
