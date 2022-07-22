package com.example.crudapplicationsql.controller;

import com.example.crudapplicationsql.dto.RequestBookDto;
import com.example.crudapplicationsql.dto.ResponseBookDto;
import com.example.crudapplicationsql.entity.Book;
import com.example.crudapplicationsql.service.BookService;
import com.example.crudapplicationsql.utils.Utils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    // Save operation
    @PostMapping("/book")
    public ResponseBookDto saveBook(@RequestBody RequestBookDto requestBookDto) {
        // Dto to Entity Conversion
        Book book = Book.builder()
                    .bookId(UUID.randomUUID().toString())
                    .creationDate(new Date())
                    .isDeleted(false)
                    .build();
        BeanUtils.copyProperties(requestBookDto, book);
        Book bookResponse = bookService.saveBook(book);
        BeanUtils.copyProperties(bookResponse , requestBookDto);

        ResponseBookDto responseBookDto = new ResponseBookDto();
        responseBookDto.setBooks(Collections.singletonList(requestBookDto));

        return responseBookDto;
    }

    // Fetch operation
    @GetMapping("/book/getAll")
    public ResponseBookDto fetchBooks() {
        List<Book> bookList = bookService.fetchBookList();
        return Utils.convertEntityToDtoList(bookList);
    }

    // Update operation
    @PutMapping("/book/{id}")
    public void updateBook(@RequestBody RequestBookDto requestBookDto, @PathVariable("id") String bookId) {
        Book book = Book.builder()
                .bookId(bookId)
                .build();
        BeanUtils.copyProperties(requestBookDto, book);
        bookService.updateBook(book);
//        BeanUtils.copyProperties(bookResponse , requestBookDto);
//
//        return ResponseBookDto.builder().books(Collections.singletonList(requestBookDto))
//                .build();
    }

    // Delete operation
    @DeleteMapping("/book/{id}")
    public HttpStatus deleteBookById(@PathVariable("id") String bookId) {
        return (bookService.deleteBookById(bookId));
    }

    // Fetch Books Based on Rating
    @GetMapping("/book/rating/{rating}")
    public ResponseBookDto fetchBooksByRating(@PathVariable("rating") Integer rating) {
        List<Book> bookList = bookService.fetchBooksByRating(rating);
        return Utils.convertEntityToDtoList(bookList);
    }

    @GetMapping("/book/{id}")
    public ResponseBookDto fetchBookById(@PathVariable("id") String bookId){
        ResponseBookDto responseBookDto = new ResponseBookDto();
        RequestBookDto requestBookDto = new RequestBookDto();
        Book book = bookService.fetchBookById(bookId);
        BeanUtils.copyProperties(book, requestBookDto);
        responseBookDto.setBooks(Collections.singletonList(requestBookDto));

        return responseBookDto;
    }
}
