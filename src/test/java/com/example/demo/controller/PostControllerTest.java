package com.example.demo.controller;

import com.example.demo.dto.CreatePostRequest;
import com.example.demo.entity.Profile;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PostRepository postRepository;

    private Profile testUser;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        profileRepository.deleteAll();
        testUser = Profile.builder()
                .username("testuser")
                .email("test@example.com")
                .passwordHash("hashed_password")
                .displayName("Test User")
                .build();
        profileRepository.save(testUser);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void shouldCreatePostSuccessfully() throws Exception {
        CreatePostRequest request = CreatePostRequest.builder()
                .content("Hello world!")
                .originalLanguage("en")
                .build();

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Hello world!"))
                .andExpect(jsonPath("$.author.username").value("testuser"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void shouldGetFeedSuccessfully() throws Exception {
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
