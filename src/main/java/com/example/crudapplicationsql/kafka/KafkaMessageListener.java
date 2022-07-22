package com.example.crudapplicationsql.kafka;

import com.example.crudapplicationsql.entity.Book;
import com.example.crudapplicationsql.repository.mysql.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class KafkaMessageListener {
    @Autowired
    private BookRepository bookRepository;

//    @KafkaListener(groupId = "${consumer_group_id}", topics = "${kafka_topic}", containerFactory = "bookKafkaListenerContainerFactory")
    public void fetchBookDetailsFromTopic(String bookString)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        Book book = null;
        try {
            book = objectMapper.readValue(bookString, Book.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Optional<Book> bookDB = bookRepository.findById(book.getBookId());
        if(bookDB.isEmpty() || Boolean.TRUE.equals(bookDB.get().getIsDeleted())) {
            log.error("No Book Found to Update");
            return;
        }
        if(Objects.nonNull(book.getBookName())) {
            bookDB.get().setBookName(book.getBookName());
        }
        if(Objects.nonNull(book.getCost())) {
            bookDB.get().setCost(book.getCost());
        }
        if(Objects.nonNull(book.getBookRating())) {
            bookDB.get().setBookRating(book.getBookRating());
        }

        bookDB.get().setLastUpdatedDate(new Date());
        bookRepository.save(bookDB.get());

        log.info("Updated book details: {}" , bookDB.get());
    }
}
