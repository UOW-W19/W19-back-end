package com.example.demo.controller;

import com.example.demo.dto.CreateWordRequest;
import com.example.demo.dto.UpdateWordRequest;
import com.example.demo.entity.Language;
import com.example.demo.entity.Profile;
import com.example.demo.entity.SavedWord;
import com.example.demo.enums.SourceType;
import com.example.demo.repository.*;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SavedWordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private SavedWordRepository savedWordRepository;

    @Autowired
    private LanguageRepository languageRepository;

    private Profile testUser;
    private Language japanese;

    @BeforeEach
    void setUp() {
        savedWordRepository.deleteAll();
        profileRepository.deleteAll();
        languageRepository.deleteAll();

        // Create test user
        testUser = Profile.builder()
                .username("testuser")
                .email("test@example.com")
                .passwordHash("hashed_password")
                .displayName("Test User")
                .build();
        profileRepository.save(testUser);

        // Create test language
        japanese = Language.builder()
                .code("ja")
                .name("Japanese")
                .flagEmoji("🇯🇵")
                .build();
        languageRepository.save(japanese);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void saveWord_Success() throws Exception {
        CreateWordRequest request = new CreateWordRequest();
        request.setWord("さくら");
        request.setTranslation("cherry blossom");
        request.setLanguageCode("ja");
        request.setSource(SourceType.MANUAL);

        mockMvc.perform(post("/api/words")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.word").value("さくら"))
                .andExpect(jsonPath("$.translation").value("cherry blossom"))
                .andExpect(jsonPath("$.language_code").value("ja"))
                .andExpect(jsonPath("$.mastery_level").value(0));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void saveWord_Duplicate_Returns500() throws Exception {
        // Save word first
        SavedWord existing = SavedWord.builder()
                .user(testUser)
                .word("さくら")
                .translation("cherry blossom")
                .languageCode("ja")
                .build();
        savedWordRepository.save(existing);

        // Try to save duplicate
        CreateWordRequest request = new CreateWordRequest();
        request.setWord("さくら");
        request.setTranslation("cherry blossom");
        request.setLanguageCode("ja");

        mockMvc.perform(post("/api/words")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getWords_WithPagination_Success() throws Exception {
        // Create test words
        SavedWord word1 = SavedWord.builder()
                .user(testUser)
                .word("さくら")
                .translation("cherry blossom")
                .languageCode("ja")
                .masteryLevel(50)
                .build();
        savedWordRepository.save(word1);

        SavedWord word2 = SavedWord.builder()
                .user(testUser)
                .word("こんにちは")
                .translation("hello")
                .languageCode("ja")
                .masteryLevel(75)
                .build();
        savedWordRepository.save(word2);

        mockMvc.perform(get("/api/words")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getWords_FilterByLanguage_Success() throws Exception {
        // Create Japanese word
        SavedWord japaneseWord = SavedWord.builder()
                .user(testUser)
                .word("さくら")
                .languageCode("ja")
                .build();
        savedWordRepository.save(japaneseWord);

        // Create Spanish language and word
        Language spanish = Language.builder()
                .code("es")
                .name("Spanish")
                .flagEmoji("🇪🇸")
                .build();
        languageRepository.save(spanish);

        SavedWord spanishWord = SavedWord.builder()
                .user(testUser)
                .word("hola")
                .languageCode("es")
                .build();
        savedWordRepository.save(spanishWord);

        mockMvc.perform(get("/api/words")
                .param("language", "ja"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].language_code").value("ja"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getWord_Success() throws Exception {
        SavedWord word = SavedWord.builder()
                .user(testUser)
                .word("さくら")
                .translation("cherry blossom")
                .languageCode("ja")
                .build();
        SavedWord saved = savedWordRepository.save(word);

        mockMvc.perform(get("/api/words/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.word").value("さくら"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void updateWord_Success() throws Exception {
        SavedWord word = SavedWord.builder()
                .user(testUser)
                .word("さくら")
                .translation("cherry blossom")
                .languageCode("ja")
                .build();
        SavedWord saved = savedWordRepository.save(word);

        UpdateWordRequest request = new UpdateWordRequest();
        request.setTranslation("sakura flower");

        mockMvc.perform(patch("/api/words/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.translation").value("sakura flower"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void deleteWord_Success() throws Exception {
        SavedWord word = SavedWord.builder()
                .user(testUser)
                .word("さくら")
                .languageCode("ja")
                .build();
        SavedWord saved = savedWordRepository.save(word);

        mockMvc.perform(delete("/api/words/" + saved.getId()))
                .andExpect(status().isNoContent());
    }
}
