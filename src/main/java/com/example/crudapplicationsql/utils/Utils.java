package com.example.crudapplicationsql.utils;

import com.example.crudapplicationsql.dto.RequestBookDto;
import com.example.crudapplicationsql.dto.ResponseBookDto;
import com.example.crudapplicationsql.entity.Book;
import com.example.crudapplicationsql.exceptions.BookNotFoundException;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static ResponseBookDto convertEntityToDtoList(List<Book> bookList){
        List<RequestBookDto> requestBookDtoList = new ArrayList<>();
        ResponseBookDto responseBookDto = new ResponseBookDto();
        if(bookList.isEmpty()){
            throw new BookNotFoundException();
        }
        for( Book book : bookList) {
            if(Boolean.FALSE.equals(book.getIsDeleted())) {
                RequestBookDto requestBookDto = new RequestBookDto();
                BeanUtils.copyProperties(book, requestBookDto);
                requestBookDtoList.add(requestBookDto);
            }
        }
        if(requestBookDtoList.isEmpty()){
            throw new BookNotFoundException();
        }
        responseBookDto.setBooks(requestBookDtoList);
        return responseBookDto;
    }
}
