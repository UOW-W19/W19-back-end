package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateWordRequest {
    private String translation;

    @JsonProperty("source_context")
    private String sourceContext;
}
