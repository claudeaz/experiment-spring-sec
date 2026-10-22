package com.example.experiment1_spring_security.book;

import com.example.experiment1_spring_security.book.request.BookRequestDto;
import com.example.experiment1_spring_security.book.response.BookResponseDto;
import com.example.experiment1_spring_security.book.response.BorrowedBookResponseDto;
import com.example.experiment1_spring_security.common.PageResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {
    Integer createBook(BookRequestDto request, Authentication connectedUSer);

    BookResponseDto findById(Integer id);

    PageResponse<BookResponseDto> findAllBooks(int page, int size, Authentication connectedUser);

    PageResponse<BookResponseDto> findAllBooksByOwner(int page, int size, Authentication connectedUser);

    PageResponse<BorrowedBookResponseDto> findAllBorrowedBooks(int page, int size, Authentication connectedUser);

    PageResponse<BorrowedBookResponseDto> findAllReturnedBooks(int page, int size, Authentication connectedUser);

    Integer updateShareableStatus(Integer bookId, Authentication connectedUser);

    Integer updateArchivedStatus(Integer bookId, Authentication connectedUser);

    Integer borrowBook(Integer bookId, Authentication connectedUser);

    Integer returnBook(Integer bookId, Authentication connectedUser);

    Integer approveReturnBook(Integer bookId, Authentication connectedUser);

    void uploadBookCover(Authentication connectedUser, Integer bookId, MultipartFile file);
}
