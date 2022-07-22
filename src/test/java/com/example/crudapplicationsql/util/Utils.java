package com.example.crudapplicationsql.util;

import com.example.crudapplicationsql.dto.RequestBookDto;
import com.example.crudapplicationsql.dto.ResponseBookDto;
import com.example.crudapplicationsql.entity.Book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Utils {
    public static List<Book> getBookListForTest() {
        List<Book> testBookList = new ArrayList<>();
        testBookList.add(new Book("1","Data Structure", "Made Easy", 2005, 1278.22 , 5, new Date(), null, false));
        testBookList.add(new Book("2","Clean Code", "Robert Martin", 1980, 1300.89 , 5, new Date(), null, false));
        testBookList.add(new Book("3","Peace of Mind", "Thomas Austin", 2000, 569.12 , 5, new Date(), null, false));
        return testBookList;
    }

    public static Book getBookForTest(){
        return new Book("1","Data Structure", "Made Easy", 2005, 1278.22 , 5, new Date(), null, false);
    }

    public static ResponseBookDto getBookResponseDtoForTest(){
        return new ResponseBookDto(List.of(new RequestBookDto("Data Structure", "Made Easy", 2005, 1278.22 , 5)));
    }

    public static ResponseBookDto getBookListResponseDtoForTest(){
        return new ResponseBookDto(List.of(new RequestBookDto("Data Structure", "Made Easy", 2005, 1278.22 , 5),
                new RequestBookDto("Clean Code", "Robert Martin", 1980, 1300.89 , 5),
                new RequestBookDto("Peace of Mind", "Thomas Austin", 2000, 569.12 , 5)));
    }
}
