package com.example.experiment1_spring_security.book.response;

import lombok.Builder;

@Builder
public record BookResponseDto(
        Integer id,
        String title,
        String authorName,
        String isbn,
        String synopsis,
        String owner,
        byte[] cover,
        double rate,
        boolean archived,
        boolean shareable
) {
}
