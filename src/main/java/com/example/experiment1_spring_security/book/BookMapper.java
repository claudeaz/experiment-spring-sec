package com.example.experiment1_spring_security.book;

import com.example.experiment1_spring_security.book.request.BookRequestDto;
import com.example.experiment1_spring_security.book.response.BookResponseDto;
import com.example.experiment1_spring_security.book.response.BorrowedBookResponseDto;
import com.example.experiment1_spring_security.file.FileUtils;
import com.example.experiment1_spring_security.history.BookTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public Book toBook(BookRequestDto request) {
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .authorName(request.authorName())
                .isbn(request.isbn())
                .synopsis(request.synopsis())
                .shareable(request.shareable())
                .archived(false)
                .build();
    }

    public BookResponseDto toBookResponse(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .rate(book.getRate())
                .synopsis(book.getSynopsis())
                .owner(book.getOwner().fullName())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .cover(FileUtils.readFileFromLocation(book.getBookCover()))
                .build();
    }

    public BorrowedBookResponseDto toBorrowedBookResponse(BookTransactionHistory history) {
        return BorrowedBookResponseDto.builder()
                .id(history.getBook().getId())
                .title(history.getBook().getTitle())
                .authorName(history.getBook().getAuthorName())
                .isbn(history.getBook().getIsbn())
                .rate(history.getBook().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}
