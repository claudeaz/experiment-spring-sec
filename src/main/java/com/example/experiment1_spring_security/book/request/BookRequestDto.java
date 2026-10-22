package com.example.experiment1_spring_security.book.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BookRequestDto(
        Integer id,

        @NotNull
        @NotEmpty
        String title,

        @NotNull
        @NotEmpty
        String authorName,

        @NotNull
        @NotEmpty
        String isbn,

        @NotNull
        @NotEmpty
        String synopsis,

        boolean shareable
) {
}
