package com.example.crudapplicationsql.rmq;

import com.example.crudapplicationsql.entity.Book;
import com.example.crudapplicationsql.repository.mysql.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class RmqMessageListener {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

//    @RabbitListener(queues = "${queue.name}")

    public void getBookDetailsFromQueue(Book book){
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

//    @RabbitListener(queues = "${queue.name}.dlq")
    public void processFailedMessages(Book failedBookUpdate) {
        log.info("Received failed Book Update Details: {}", failedBookUpdate.toString());
    }
}
