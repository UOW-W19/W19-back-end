package com.example.demo.config;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.example.demo.entity.Language;
import com.example.demo.entity.Post;
import com.example.demo.entity.Profile;
import com.example.demo.entity.UserLanguage;
import com.example.demo.enums.PostStatus;
import com.example.demo.repository.LanguageRepository;
import com.example.demo.repository.PostRepository;
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
    private final PostRepository postRepository;

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

        // Seed posts if there are none (separate check so posts get created even if
        // users exist)
        if (postRepository.count() == 0) {
            log.info("Seeding sample posts...");

            Profile demoProfile = profileRepository.findByEmail("demo@locale.app").orElse(null);
            if (demoProfile == null) {
                log.warn("Demo user not found, skipping post seeding");
                return;
            }

            // Seed some sample posts
            Post post1 = Post.builder()
                    .author(demoProfile)
                    .content("Hello world! This is my first post on Locale 🌍")
                    .originalLanguage("en")
                    .status(PostStatus.ACTIVE)
                    .latitude(-33.8688)
                    .longitude(151.2093)
                    .build();

            Post post2 = Post.builder()
                    .author(demoProfile)
                    .content("¡Hola! Estoy aprendiendo español. ¿Alguien quiere practicar conmigo?")
                    .originalLanguage("es")
                    .status(PostStatus.ACTIVE)
                    .latitude(-33.8688)
                    .longitude(151.2093)
                    .build();

            Post post3 = Post.builder()
                    .author(demoProfile)
                    .content("Looking for language exchange partners in Sydney! Coffee anyone? ☕")
                    .originalLanguage("en")
                    .status(PostStatus.ACTIVE)
                    .latitude(-33.8688)
                    .longitude(151.2093)
                    .build();

            postRepository.saveAll(List.of(post1, post2, post3));

            log.info("Created {} sample posts", 3);
        }
    }
}
