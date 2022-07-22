package com.example.crudapplicationsql.repository.mysql;

import com.example.crudapplicationsql.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book , String> {

    // native sql query
    @Query(value="select * from book b where b.book_rating=?1", nativeQuery = true)
    List<Book> findByBookRating(Integer rating);
}
