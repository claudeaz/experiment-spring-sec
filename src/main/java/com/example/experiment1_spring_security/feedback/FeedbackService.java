package com.example.experiment1_spring_security.feedback;

import com.example.experiment1_spring_security.common.PageResponse;
import com.example.experiment1_spring_security.feedback.request.FeedbackRequestDto;
import com.example.experiment1_spring_security.feedback.response.FeedbackResponseDto;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

public interface FeedbackService {
    Integer saveFeedBack(@Valid FeedbackRequestDto requestDto, Authentication connectedUser);

    PageResponse<FeedbackResponseDto> findAllFeedbacksByBook(Integer bookId, int page, int size, Authentication connectedUser);
}
