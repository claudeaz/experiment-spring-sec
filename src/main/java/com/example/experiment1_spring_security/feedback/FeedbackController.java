package com.example.experiment1_spring_security.feedback;

import com.example.experiment1_spring_security.common.PageResponse;
import com.example.experiment1_spring_security.feedback.request.FeedbackRequestDto;
import com.example.experiment1_spring_security.feedback.response.FeedbackResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback")
public class FeedbackController {

    private final FeedbackService feedBackService;

    @PostMapping
    public ResponseEntity<Integer> saveFeedBack(
            @Valid @RequestBody FeedbackRequestDto requestDto,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(feedBackService.saveFeedBack(requestDto, connectedUser));
    }

    @GetMapping("/books/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponseDto>> findAllFeedbacksByBook(
            @PathVariable("book-id") Integer bookId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "0", required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(feedBackService.findAllFeedbacksByBook(bookId, page, size, connectedUser));
    }
}
