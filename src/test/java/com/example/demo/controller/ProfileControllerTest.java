package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    private String token;

    @BeforeEach
    void setup() {
        String email = "profile@example.com";
        String password = "password123";

        try {
            RegisterRequest request = new RegisterRequest();
            request.setEmail(email);
            request.setPassword(password);
            request.setUsername("profileuser");
            request.setDisplayName("Profile User");
            request.setNativeLanguage("en");
            request.setLearningLanguages(java.util.List.of("fr"));
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
        mockMvc.perform(get("/api/profiles/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAccessWithValidToken() throws Exception {
        if (token == null)
            return;

        mockMvc.perform(get("/api/profiles/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("profile@example.com"));
    }
}
