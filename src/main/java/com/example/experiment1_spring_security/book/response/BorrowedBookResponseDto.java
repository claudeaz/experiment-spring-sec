package com.example.experiment1_spring_security.book.response;

import lombok.Builder;

@Builder
public record BorrowedBookResponseDto(
        Integer id,
        String title,
        String authorName,
        String isbn,
        double rate,
        boolean returned,
        boolean returnApproved
) {
}
