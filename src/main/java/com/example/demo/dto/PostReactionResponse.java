package com.example.demo.dto;

import com.example.demo.enums.ReactionType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostReactionResponse {
    private long likes;
    private long comments;
    private ReactionType userReaction;
}
