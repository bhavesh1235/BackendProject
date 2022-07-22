package com.example.crudapplicationsql.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book implements Serializable {
    @Id
    private String bookId;

    private String bookName;

    private String author;

    private Integer year;

    private Double cost;

    private Integer bookRating;

    private Date creationDate;

    private Date lastUpdatedDate;

    private Boolean isDeleted;
}
