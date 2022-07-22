package com.example.crudapplicationsql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class CrudApplicationSqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudApplicationSqlApplication.class, args);
    }

}
