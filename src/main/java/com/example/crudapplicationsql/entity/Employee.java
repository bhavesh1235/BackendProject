package com.example.crudapplicationsql.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @JsonProperty("id")
    private String id;
    @JsonProperty("FirstName")
    private String FirstName;
    @JsonProperty("LastName")
    private String LastName;
    @JsonProperty("Designation")
    private String Designation;
    @JsonProperty("Salary")
    private Integer Salary;
    @JsonProperty("DateOfJoining")
    private String  DateOfJoining;
    @JsonProperty("Address")
    private String Address;
    @JsonProperty("Gender")
    private String Gender;
    @JsonProperty("Age")
    private Integer Age;
    @JsonProperty("MaritalStatus")
    private String MaritalStatus;
    @JsonProperty("Interests")
    private String Interests;
}
