package com.example.crudapplicationsql.controller;

import com.example.crudapplicationsql.entity.Employee;
import com.example.crudapplicationsql.service.EmployeeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @ResponseBody
    @GetMapping("/employee/address/{address}")
    public ResponseEntity<List<Employee>> fetchEmployeeListByAddress(@PathVariable("address") String address){
        List<Employee> employeeList = employeeService.searchByAddress(address);
        return new ResponseEntity<>(employeeList, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/employee/bulkRequest/")
    public void bulkRequest(){
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<Map<String , List<Employee>>> typeReference = new TypeReference<Map<String , List<Employee>>>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/data/EmployeeData.json");
        try {
            Map<String , List<Employee>> employeeMap = mapper.readValue(inputStream,typeReference);
            employeeService.bulkUploadToElasticSearch(employeeMap.get("empList"));
        } catch (IOException e){
            throw new RuntimeException();
        }
    }


    //@ResponseBody
//    @GetMapping("/employee/{id}")
//    public ResponseEntity<Employee> fetchEmployeeById(@PathVariable("id") String id){
//        Employee employee = employeeService.findOne(id);
//        return new ResponseEntity<>(employee, HttpStatus.OK);
//    }

//    @ResponseBody
//    @GetMapping("/employee/multiQuery/{firstname}/{age}")
//    public ResponseEntity<SearchHits<Employee>> fetchEmployeesByAddress(@PathVariable("firstname") String firstname,
//                                                                        @PathVariable("age") Integer age){
//        SearchHits<Employee> searchHits = employeeService.searchMultiField(firstname, age);
//        return new ResponseEntity<>(searchHits, HttpStatus.OK);
//    }
//
//    @ResponseBody
//    @GetMapping("/employee/name/{input}")
//    public ResponseEntity<SearchHits<Employee>> fetchEmployeesByFirstNameRegex(@PathVariable("input") String input){
//        SearchHits<Employee> searchHits = employeeService.getEmployeeSearchData(input);
//        return new ResponseEntity<>(searchHits, HttpStatus.OK);
//    }
//
//    @ResponseBody
//    @GetMapping("/employee/find/{salary}/{designation}")
//    public ResponseEntity<SearchHits<Employee>> fetchEmployeesBySalaryAndDesignation(@PathVariable("salary") Integer salary,
//                                                                                     @PathVariable("designation") String designation){
//        SearchHits<Employee> searchHits = employeeService.fetchEmployeesBySalaryAndDesignation(salary, designation);
//        return new ResponseEntity<>(searchHits, HttpStatus.OK);
//    }
//
//    @ResponseBody
//    @GetMapping("/employee/count/{inputField}")
//    public ResponseEntity<Map<String, Long>> fetchEmployeesCountBasedOnInputField(@PathVariable("inputField") String inputField){
//        Map<String, Long> searchHits = employeeService.fetchEmployeesCountBasedOnField(inputField);
//        return new ResponseEntity<>(searchHits, HttpStatus.OK);
//    }
//
//    @ResponseBody
//    @GetMapping("/employee/fuzzySearch/{query}")
//    public ResponseEntity<SearchHits<Employee>> fetchEmployeesByFuzzySearch(@PathVariable("query") String query){
//        SearchHits<Employee> searchHits = employeeService.processSearch(query);
//        return new ResponseEntity<>(searchHits, HttpStatus.OK);
//    }
}
