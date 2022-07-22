package com.example.crudapplicationsql.service;

import com.example.crudapplicationsql.entity.Employee;
import com.example.crudapplicationsql.repository.elasticsearch.EmployeeElasticSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class EmployeeServiceImplementation implements EmployeeService {

    @Autowired
    EmployeeElasticSearchRepository employeeElasticSearchRepository;
    @Autowired
    BulkProcessor bulkProcessor;
    @Autowired
    RestHighLevelClient client;
    @Override
    public List<Employee> searchByAddress(String address) {
        return employeeElasticSearchRepository.searchByAddress(address);
    }

    @Override
    public void bulkUploadToElasticSearch(List<Employee> employeeList) {
        employeeElasticSearchRepository.employeeBulkUpload(employeeList);
    }

//    @Override
//    public SearchHits<Employee> searchMultiField(String firstname, Integer age) {
//        QueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("FirstName", firstname))
//                .should(QueryBuilders.matchQuery("Age", age));
//        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(query).build();
//        return elasticsearchOperations.search(nativeSearchQuery, Employee.class);
//    }
//
//    @Override
//    public SearchHits<Employee> getEmployeeSearchData(String input) {
//        QueryBuilder query = QueryBuilders.boolQuery()
//                .should(QueryBuilders.queryStringQuery(input)
//                        .field("FirstName")
//                        .field("LastName"))
//                .should(QueryBuilders.queryStringQuery("*" + input + "*")
//                        .field("FirstName")
//                        .field("LastName"));
//        Query searchQuery = new NativeSearchQueryBuilder().withQuery(query).build();
//        return elasticsearchOperations.search(searchQuery, Employee.class);
//    }
//
//    @Override
//    public SearchHits<Employee> fetchEmployeesBySalaryAndDesignation(Integer salary, String designation) {
//        Criteria criteria = new Criteria("Salary").greaterThanEqual(salary - 10000)
//                .lessThanEqual(salary + 10000).subCriteria(new Criteria("Designation").matches(designation));
//        Query query = new CriteriaQuery(criteria);
//        return elasticsearchOperations.search(query, Employee.class);
//
//    }
//
//    @Override
//    public Map<String, Long> fetchEmployeesCountBasedOnField(String inputField) {
//        TermsAggregationBuilder termsAggregationBuilder = new TermsAggregationBuilder(inputField).field(inputField).size(10);
//        Query searchQuery = new NativeSearchQueryBuilder().withAggregations(termsAggregationBuilder)
//                .build();
//
//
//        ElasticsearchAggregations elasticsearchAggregations = (ElasticsearchAggregations) elasticsearchOperations.search(searchQuery, Employee.class).getAggregations();
//        Map<String, Long> result = new HashMap<>();
//        assert elasticsearchAggregations != null;
//        Aggregations aggregations = elasticsearchAggregations.aggregations();
//        aggregations.asList().forEach(aggregation -> {
//            ((Terms) aggregation).getBuckets().forEach(bucket -> result.put(bucket.getKeyAsString(), bucket.getDocCount()));
//        });
//        return result;
//    }
//
//    @Override
//    public SearchHits<Employee> processSearch(String query) {
//        log.info("Search with query {}", query);
//
//        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(query, "FirstName", "LastName")
//                .fuzziness(2);
//
//        Query searchQuery = new NativeSearchQueryBuilder().withFilter(queryBuilder).build();
//        return elasticsearchOperations.search(searchQuery, Employee.class);
//
//    }
}
