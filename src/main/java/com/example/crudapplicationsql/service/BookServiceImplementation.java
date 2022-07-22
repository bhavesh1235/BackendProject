package com.example.crudapplicationsql.service;

import com.example.crudapplicationsql.entity.Book;
import com.example.crudapplicationsql.exceptions.BookNotFoundException;
import com.example.crudapplicationsql.kafka.KafkaMessageProducer;
import com.example.crudapplicationsql.repository.mysql.BookRepository;
import com.example.crudapplicationsql.repository.redis.RedisCacheBookRepository;
import com.example.crudapplicationsql.rmq.RmqMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImplementation implements BookService{

    @Autowired
    private RmqMessageProducer messageProducer;

    @Autowired
    private KafkaMessageProducer kafkaMessageProducer;
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RedisCacheBookRepository redisBookRepository;

    public BookServiceImplementation(BookRepository bookRepository, RedisCacheBookRepository redisBookRepository) {
        this.bookRepository=bookRepository;
        this.redisBookRepository=redisBookRepository;
    }

    @Override
    public Book saveBook(Book book) {
        bookRepository.save(book);
        log.info("Book Details Successfully Saved : {} ",book);
        return book;
    }

    @Override
    public List<Book> fetchBookList() {
        List<Book> redisBookList = redisBookRepository.findAll();
        if(redisBookList != null){
            return redisBookList;
        }
        log.info("All books fetched");
        List<Book> bookList = bookRepository.findAll();
        if(bookList.isEmpty()) {
            throw new BookNotFoundException();
        }
        redisBookRepository.saveBookList(bookList);
        return bookList;
    }

    @Override
    public void updateBook(Book book) {
        Optional<Book> bookDB = bookRepository.findById(book.getBookId());
        if(bookDB.isEmpty() || Boolean.TRUE.equals(bookDB.get().getIsDeleted())) {
            log.error("No Book Found to Update with bookId {}",book.getBookId());
            throw new BookNotFoundException();
        }
        kafkaMessageProducer.sendBookDetailsToTopic(book);
    }

    @Override
    public HttpStatus deleteBookById(String bookId) {
        Book redisBook = redisBookRepository.findById(bookId);
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            log.error("No Book Found to Delete with bookId {} ",bookId);
            throw new BookNotFoundException();
        }
        book.get().setIsDeleted(true);
        bookRepository.save(book.get());
        if(redisBook != null){
            redisBookRepository.delete(bookId);
        }
        log.info("Book with"+bookId+" deleted successfully");
        return HttpStatus.OK;
    }

    @Override
    public List<Book> fetchBooksByRating(Integer rating) {
        List<Book> redisBookList = redisBookRepository.findByRating(rating);
        if(redisBookList != null){
            return redisBookList;
        }
        log.info("Getting book with rating {}.", rating);
        List<Book> bookList= bookRepository.findByBookRating(rating);
        if(bookList.isEmpty()){
            log.error("No Book Found with rating {} ",rating);
            throw new BookNotFoundException();
        }
        redisBookRepository.saveBookListBasedOnRating(bookList, rating);
        return bookList;
    }

    @Override
    public Book fetchBookById(String bookId) {
        Book redisBook = redisBookRepository.findById(bookId);
        if(redisBook != null) {
            return redisBook;
        }
        log.info("Getting book with ID {}.", bookId);
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty() || Boolean.TRUE.equals(book.get().getIsDeleted())) {
            log.error("No Book Found with bookId {} ", bookId);
            throw new BookNotFoundException();
        }
        log.info("book {}", book.get());
        redisBookRepository.saveBook(book.get());
        return book.get();
    }
}
