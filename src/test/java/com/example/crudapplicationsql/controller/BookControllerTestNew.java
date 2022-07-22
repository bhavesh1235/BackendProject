package com.example.crudapplicationsql.controller;

import com.example.crudapplicationsql.dto.ResponseBookDto;
import com.example.crudapplicationsql.entity.Book;
import com.example.crudapplicationsql.service.BookService;
import com.example.crudapplicationsql.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
@Slf4j
class BookControllerTestNew {
    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;
    private List<Book> expectedBookList;
    private ResponseBookDto expectedBookDtoList;
    private ResponseBookDto expectedBookDto;
    private Book expectedBook;

    @BeforeEach
    void setUp() {
        this.bookController = new BookController(bookService);
        this.expectedBookDto = Utils.getBookResponseDtoForTest();
        expectedBook = Utils.getBookForTest();
        expectedBookList = Utils.getBookListForTest();
        expectedBookDtoList = Utils.getBookListResponseDtoForTest();
    }
    @Test
    public void fetchAllBooks() {
        Mockito.when(bookService.fetchBookList()).thenReturn(expectedBookList);
        ResponseBookDto actualBookList = bookController.fetchBooks();
        assertThat(actualBookList).isEqualTo(expectedBookDtoList);
        log.info(" All Books Fetched : {}", actualBookList);
    }
    @Test
    public void fetchBookById(){
        Mockito.when(bookService.fetchBookById(expectedBook.getBookId())).thenReturn(expectedBook);
        ResponseBookDto actualBook = bookController.fetchBookById(expectedBook.getBookId());
        assertThat(actualBook).isEqualTo(expectedBookDto);
        log.info("Book Response : {}", actualBook);
    }
}
