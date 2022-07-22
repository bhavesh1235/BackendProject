package com.example.crudapplicationsql.service;

import com.example.crudapplicationsql.entity.Book;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface BookService {

    // Save operation
    Book saveBook(Book book);

    // Read operation
    List<Book> fetchBookList();

    // Update operation
    void updateBook(Book book);

    // Delete operation
    HttpStatus deleteBookById(String bookId);

    // Fetch Books Based on Rating
    List<Book> fetchBooksByRating(Integer rating);

    Book fetchBookById(String bookId);
}
