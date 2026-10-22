package com.example.experiment1_spring_security.feedback.response;

import lombok.Builder;

@Builder
public record FeedbackResponseDto(
        Double note,
        String comment,
        Boolean ownFeedback
) {
}
