package com.example.crudapplicationsql.service;

import com.example.crudapplicationsql.entity.Book;
import com.example.crudapplicationsql.repository.mysql.BookRepository;
import com.example.crudapplicationsql.repository.redis.RedisCacheBookRepository;
import com.example.crudapplicationsql.service.impl.BookServiceImplementation;
import com.example.crudapplicationsql.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    // System under Test (SUT)
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private RedisCacheBookRepository redisCacheBookRepository;

    private List<Book> expectedBookList;
    private Book expectedBook;
    private Integer bookRating;

    @BeforeEach
    void setup(){
        this.bookService = new BookServiceImplementation(this.bookRepository, this.redisCacheBookRepository) {
        };
        expectedBook = Utils.getBookForTest();
        expectedBookList = Utils.getBookListForTest();
        bookRating = 5;
    }
    @Test
    void fetchBookByIdWhenCacheIsEmpty(){
        when(redisCacheBookRepository.findById(expectedBook.getBookId())).thenReturn(null);
        when(bookRepository.findById(expectedBook.getBookId())).thenReturn(Optional.of(expectedBook));
        Book actualBook = bookService.fetchBookById(expectedBook.getBookId());
        assertNotNull(actualBook);
        assertEquals(expectedBook, actualBook);
        log.info("Fetched Book From DB : {}", actualBook);
    }
    @Test
    void fetchBookByIdWhenCacheIsNotEmpty() {
        when(redisCacheBookRepository.findById(expectedBook.getBookId())).thenReturn(expectedBook);
        Book actualBook = bookService.fetchBookById(expectedBook.getBookId());
        assertNotNull(actualBook);
        assertEquals(expectedBook, actualBook);
        verify(bookRepository , never()).findById(anyString());
        log.info("Fetched Book From Redis Cache : {}", actualBook);
    }

    @Test
    void fetchAllBooksWhenCacheIsEmpty(){
        when(redisCacheBookRepository.findAll()).thenReturn(null);
        when(bookRepository.findAll()).thenReturn(expectedBookList);
        List<Book> actualBookList = bookService.fetchBookList();
        assertNotNull(actualBookList);
        assertEquals(expectedBookList, actualBookList);
        log.info("Fetched Books List From DB , {}", actualBookList);
    }
    @Test
    void fetchAllBooksWhenCacheIsNotEmpty() {
        when(redisCacheBookRepository.findAll()).thenReturn(expectedBookList);
        List<Book> actualBookList = bookService.fetchBookList();
        assertNotNull(actualBookList);
        assertEquals(expectedBookList, actualBookList);
        log.info("Fetched From Redis Cache");
    }
    @Test
    void fetchBooksListBasedOnRatingWhenCacheIsEmpty(){
        when(redisCacheBookRepository.findByRating(bookRating)).thenReturn(null);
        when(bookRepository.findByBookRating(bookRating)).thenReturn(expectedBookList);
        List<Book> actualBookList = bookService.fetchBooksByRating(5);
        assertNotNull(actualBookList);
        assertEquals(expectedBookList, actualBookList);
        log.info("Fetched Books List Based On rating From DB : {}", actualBookList);
    }
    @Test
    void fetchBooksListBasedOnRatingWhenCacheIsNotEmpty() {
        when(redisCacheBookRepository.findByRating(bookRating)).thenReturn(expectedBookList);
        List<Book> actualBookList = bookService.fetchBooksByRating(5);
        assertNotNull(actualBookList);
        assertEquals(expectedBookList, actualBookList);
        log.info("Fetched Books List Based On rating From Redis : {}", actualBookList);
    }


    @Test
    void deleteBook() {
        HttpStatus expected = HttpStatus.OK;
        when(bookRepository.findById(expectedBook.getBookId())).thenReturn(Optional.of(expectedBook));
        when(bookRepository.save(expectedBook)).thenReturn(expectedBook);
        HttpStatus actual = bookService.deleteBookById(expectedBook.getBookId());
        assertEquals(expected, actual);
        log.info("Book deleted successfully : {}", expectedBook);
    }

    @Test
    void saveBook(){
        when(bookRepository.save(expectedBook)).thenReturn(expectedBook);
        assertEquals(expectedBook, bookService.saveBook(expectedBook));
        log.info("Book Saved successfully : {}", expectedBook);
    }
}
