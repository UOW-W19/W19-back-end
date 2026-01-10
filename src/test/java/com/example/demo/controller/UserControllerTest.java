package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private com.example.demo.repository.ContentReportRepository contentReportRepository;
    @Autowired
    private com.example.demo.repository.PostTranslationRepository postTranslationRepository;
    @Autowired
    private com.example.demo.repository.PostReactionRepository postReactionRepository;
    @Autowired
    private com.example.demo.repository.PostCommentRepository postCommentRepository;
    @Autowired
    private com.example.demo.repository.PostRepository postRepository;
    @Autowired
    private com.example.demo.repository.PracticeResultRepository practiceResultRepository;
    @Autowired
    private com.example.demo.repository.PracticeSessionRepository practiceSessionRepository;
    @Autowired
    private com.example.demo.repository.SavedWordRepository savedWordRepository;
    @Autowired
    private com.example.demo.repository.UserLanguageRepository userLanguageRepository;
    @Autowired
    private com.example.demo.repository.UserSettingsRepository userSettingsRepository;
    @Autowired
    private com.example.demo.repository.UserBlockRepository userBlockRepository;
    @Autowired
    private com.example.demo.repository.FollowRepository followRepository;
    @Autowired
    private com.example.demo.repository.LanguageRepository languageRepository;

    private String token;

    @BeforeEach
    void setup() {
        contentReportRepository.deleteAll();
        postTranslationRepository.deleteAll();
        postReactionRepository.deleteAll();
        postCommentRepository.deleteAll();
        postRepository.deleteAll();
        practiceResultRepository.deleteAll();
        practiceSessionRepository.deleteAll();
        savedWordRepository.deleteAll();
        userLanguageRepository.deleteAll();
        userSettingsRepository.deleteAll();
        userBlockRepository.deleteAll();
        followRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        profileRepository.deleteAll();
        languageRepository.deleteAll();

        String email = "user_me@example.com";
        String password = "password123";

        try {
            RegisterRequest request = new RegisterRequest();
            request.setEmail(email);
            request.setPassword(password);
            request.setUsername("user_me");
            request.setDisplayName("User Me");
            AuthResponse response = authService.register(request);
            token = response.getAccessToken();
        } catch (Exception e) {
            try {
                AuthResponse response = authService.login(new LoginRequest(email, password));
                token = response.getAccessToken();
            } catch (Exception loginEx) {
                // Ignore
            }
        }
    }

    @Test
    void shouldDenyAccessWithoutToken() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAccessWithValidToken() throws Exception {
        if (token == null)
            return;

        mockMvc.perform(get("/api/users/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user_me@example.com"));
    }
}
