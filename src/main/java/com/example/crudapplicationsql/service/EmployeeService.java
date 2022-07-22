package com.example.crudapplicationsql.service;

import com.example.crudapplicationsql.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    List<Employee> searchByAddress(String address);

    void bulkUploadToElasticSearch(List<Employee> employeeList);

//    SearchHits<Employee> searchMultiField(String firstname, Integer age);
//
//    SearchHits<Employee> getEmployeeSearchData(String input);
//
//    SearchHits<Employee> fetchEmployeesBySalaryAndDesignation(Integer salary, String designation);
//
//    Map<String, Long> fetchEmployeesCountBasedOnField(String inputField);
//
//    SearchHits<Employee> processSearch(String query);
}
