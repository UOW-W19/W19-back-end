package com.example.demo.dto;

import com.example.demo.enums.ProficiencyLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLanguageDTO {
    private String code;
    private String name;
    private String flagEmoji;
    private ProficiencyLevel proficiency;
    private boolean isLearning;
}
