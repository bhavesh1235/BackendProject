package com.example.crudapplicationsql.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestBookDto implements Serializable {

    private String bookName;

    private String author;

    private Integer year;

    private Double cost;

    private Integer bookRating;
}
