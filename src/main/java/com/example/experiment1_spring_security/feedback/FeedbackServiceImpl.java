package com.example.experiment1_spring_security.feedback;

import com.example.experiment1_spring_security.book.Book;
import com.example.experiment1_spring_security.book.BookRepository;
import com.example.experiment1_spring_security.common.PageResponse;
import com.example.experiment1_spring_security.exception.OperationNotPermittedException;
import com.example.experiment1_spring_security.feedback.request.FeedbackRequestDto;
import com.example.experiment1_spring_security.feedback.response.FeedbackResponseDto;
import com.example.experiment1_spring_security.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final BookRepository bookRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    @Override
    public Integer saveFeedBack(FeedbackRequestDto requestDto, Authentication connectedUser) {
        Book book = bookRepository.findById(requestDto.bookId())
                .orElseThrow(()-> new EntityNotFoundException("No book found with the ID: " + requestDto.bookId()));
        if (book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("You cannot give a feedback for an archived or a not shareable book");
        }
        User user = (User) connectedUser.getPrincipal();
        if (Objects.equals(book.getOwner().getId(), user.getId())){
            throw  new OperationNotPermittedException("You cannot give a feedback to your own book");
        }

        Feedback feedback = feedbackMapper.toFeedBack(requestDto);
        feedback.setBook(book);
        return feedbackRepository.save(feedback).getId();
    }

    @Override
    public PageResponse<FeedbackResponseDto> findAllFeedbacksByBook(Integer bookId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = (User) connectedUser.getPrincipal();
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);
        List<FeedbackResponseDto> feedbackResponses = feedbacks.stream()
                .map(feedback -> feedbackMapper.toFeedbackResponse(feedback, user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
