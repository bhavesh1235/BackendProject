package com.example.crudapplicationsql.controller;

import com.example.crudapplicationsql.entity.Book;
import com.example.crudapplicationsql.service.BookService;
import com.example.crudapplicationsql.util.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private List<Book> expectedBookList;
    private Book expectedBook;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        expectedBook = Utils.getBookForTest();
        expectedBookList = Utils.getBookListForTest();
    }

    @Test
    void fetchAllBooks() {
        Mockito.when(bookService.fetchBookList()).thenReturn(expectedBookList);
        try {
            this.mockMvc.perform(get("/book/getAll"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.books.size()").value(expectedBookList.size()))
                    .andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    void fetchBookById() {
        Mockito.when(bookService.fetchBookById(expectedBook.getBookId())).thenReturn(expectedBook);
        try {
            this.mockMvc.perform(get("/book/{id}", expectedBook.getBookId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.books[0].bookName").value(expectedBook.getBookName()))
                    .andExpect(jsonPath("$.books[0].author").value(expectedBook.getAuthor()))
                    .andExpect(jsonPath("$.books[0].cost").value(expectedBook.getCost()))
                    .andExpect(jsonPath("$.books[0].year").value(expectedBook.getYear()))
                    .andExpect(jsonPath("$.books[0].bookRating").value(expectedBook.getBookRating()))
                    .andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}