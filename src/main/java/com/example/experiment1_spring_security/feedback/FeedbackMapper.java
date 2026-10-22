package com.example.experiment1_spring_security.feedback;

import com.example.experiment1_spring_security.feedback.request.FeedbackRequestDto;
import com.example.experiment1_spring_security.feedback.response.FeedbackResponseDto;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {

    public Feedback toFeedBack(FeedbackRequestDto requestDto) {
        return Feedback.builder()
                .note(requestDto.note())
                .comment(requestDto.comment())
                .build();
    }

    public FeedbackResponseDto toFeedbackResponse(Feedback feedback, Integer userId) {
        return FeedbackResponseDto.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), userId))
                .build();
    }
}
