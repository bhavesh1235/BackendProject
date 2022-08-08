package com.example.crudapplicationsql.repository.redis;

import com.example.crudapplicationsql.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class RedisCacheBookRepository {
    private final String BOOK_LIST = "BOOK_LIST";
    private final String BOOK_LIST_RATING = "BOOK_LIST_RATING_";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> bookValueOperations;
    private final ValueOperations<String, Object> bookListValueOperation;

    public RedisCacheBookRepository(RedisTemplate<String, Object> redisTemplate){
        this.bookValueOperations = redisTemplate.opsForValue();
        this.bookListValueOperation = redisTemplate.opsForValue();
        this.redisTemplate = redisTemplate;
    }

    public void saveBook(Book book){
          bookValueOperations.set(book.getBookId(), book, 10, TimeUnit.MINUTES);
          log.info("Book with Id {} Saved in Cache", book.getBookId());
    }
    public void saveBookListBasedOnRating(List<Book> bookList, Integer rating){
        bookListValueOperation.set(BOOK_LIST_RATING + rating.toString(), bookList, 5, TimeUnit.MINUTES);
        log.info("Books with Rating {} Saved in Cache", rating);
    }
    public void saveBookList(List<Book> bookList){
        bookListValueOperation.set(BOOK_LIST, bookList, 5, TimeUnit.MINUTES);
        log.info("BookList Saved To Cache");
    }

    public void update(Book book){
        saveBook(book);
    }

    public List<Book> findAll(){
        if(Boolean.FALSE.equals(redisTemplate.hasKey(BOOK_LIST)))
            return null;
        log.info("BookList Fetched From Cache");
        return (List<Book>) bookListValueOperation.get(BOOK_LIST);
    }

    public Book findById(String bookId){
        if(Boolean.FALSE.equals(redisTemplate.hasKey(bookId)))
            return null;
        log.info("Book with Id {} Fetched in Cache", bookId);
        return (Book) bookValueOperations.get(bookId);
    }

    public List<Book> findByRating(Integer rating) {
        if(Boolean.FALSE.equals(redisTemplate.hasKey(BOOK_LIST_RATING + rating.toString())))
            return null;
        log.info("BookList with Rating {} Fetched in Cache", rating);
        return (List<Book>) bookListValueOperation.get(BOOK_LIST_RATING + rating.toString());
    }
    public void delete(String bookId){
        bookValueOperations.setIfPresent(bookId, "", 1, TimeUnit.MILLISECONDS);
        log.info("Book with Id {} Deleted from Cache", bookId);
    }
}

