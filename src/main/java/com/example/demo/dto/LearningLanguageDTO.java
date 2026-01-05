package com.example.demo.dto;

import com.example.demo.enums.ProficiencyLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearningLanguageDTO {
    private String languageCode;
    private ProficiencyLevel proficiencyLevel;
}
