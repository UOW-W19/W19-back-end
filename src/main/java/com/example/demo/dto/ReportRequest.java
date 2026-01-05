package com.example.demo.dto;

import com.example.demo.enums.ReportReason;
import lombok.Data;
import java.util.UUID;

@Data
public class ReportRequest {
    private UUID postId;
    private UUID commentId;
    private ReportReason reason;
    private String description;
}
