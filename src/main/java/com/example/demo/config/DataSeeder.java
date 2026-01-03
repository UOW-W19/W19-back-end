package com.example.demo.config;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final AuthService authService;
    private final ProfileRepository profileRepository;

    @Override
    public void run(String... args) {
        if (profileRepository.count() == 0) {
            log.info("Seeding demo data...");

            RegisterRequest demoUser = new RegisterRequest();
            demoUser.setEmail("demo@locale.app");
            demoUser.setPassword("demo123");
            demoUser.setDisplayName("Demo User");
            demoUser.setNativeLanguage("en");
            demoUser.setLearningLanguages(List.of(
                    new com.example.demo.dto.LearningLanguageDTO("es", com.example.demo.enums.ProficiencyLevel.A1),
                    new com.example.demo.dto.LearningLanguageDTO("vi", com.example.demo.enums.ProficiencyLevel.B2)));

            authService.register(demoUser);

            log.info("Demo data seeded successfully! You can now login with demo@locale.app / demo123");
        }
    }
}
