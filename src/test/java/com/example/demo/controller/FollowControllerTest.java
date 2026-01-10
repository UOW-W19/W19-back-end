package com.example.demo.controller;

import com.example.demo.entity.Profile;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FollowControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ProfileRepository profileRepository;

        @Autowired
        private FollowRepository followRepository;

        @Autowired
        private JwtUtils jwtUtils;

        private String token;
        private Profile follower;
        private Profile following;

        @BeforeEach
        void setUp() {
                followRepository.deleteAll();
                profileRepository.deleteAll();

                follower = Profile.builder()
                                .email("follower@example.com")
                                .username("follower")
                                .passwordHash("hash")
                                .build();
                profileRepository.save(follower);

                following = Profile.builder()
                                .email("following@example.com")
                                .username("following")
                                .passwordHash("hash")
                                .build();
                profileRepository.save(following);

                token = "Bearer " + jwtUtils.generateToken(follower.getEmail());
        }

        @Test
        void shouldFollowUser() throws Exception {
                mockMvc.perform(post("/api/users/" + following.getId() + "/follow")
                                .header("Authorization", token))
                                .andExpect(status().isOk());

                assertEquals(1, followRepository.countByFollowerId(follower.getId()));
                assertEquals(1, followRepository.countByFollowingId(following.getId()));
        }

        @Test
        void shouldUnfollowUser() throws Exception {
                // Given
                mockMvc.perform(post("/api/users/" + following.getId() + "/follow")
                                .header("Authorization", token))
                                .andExpect(status().isOk());

                // When
                mockMvc.perform(delete("/api/users/" + following.getId() + "/follow")
                                .header("Authorization", token))
                                .andExpect(status().isOk());

                // Then
                assertEquals(0, followRepository.countByFollowerId(follower.getId()));
        }

        @Test
        void shouldNotFollowSelf() throws Exception {
                mockMvc.perform(post("/api/users/" + follower.getId() + "/follow")
                                .header("Authorization", token))
                                .andExpect(status().isBadRequest()); // Handled by GlobalExceptionHandler
        }
}
