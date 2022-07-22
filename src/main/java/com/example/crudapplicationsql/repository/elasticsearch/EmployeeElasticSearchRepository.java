package com.example.crudapplicationsql.repository.elasticsearch;

import com.example.crudapplicationsql.entity.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@Slf4j
public class EmployeeElasticSearchRepository {
    @Autowired
    private RestHighLevelClient client;

    private ObjectMapper objectMapper = new ObjectMapper();

    public List<Employee> searchByAddress(String address){
        QueryBuilder queryBuilder = new MatchQueryBuilder("Address",  address).fuzziness(Fuzziness.AUTO);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("companydatabase");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(queryBuilder).size(100);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SearchHit[] searchHit = searchResponse.getHits().getHits();
        List<Employee> employeeList = new ArrayList<>();
        for (SearchHit hit : searchHit) {
            Employee employee = objectMapper.convertValue(hit.getSourceAsMap(), Employee.class);
            employee.setId(hit.getId());
            employeeList.add(employee);
        }
        return employeeList;
    }

    public void employeeBulkUpload(List<Employee> employeeList){
        BulkRequest bulkRequest =  new BulkRequest();
        for(Employee employee : employeeList){
            log.info("{}", employee);
            Map<String, Employee> map = objectMapper.convertValue(employee, Map.class);
            IndexRequest indexRequest = new IndexRequest().index("companydatabase").id(UUID.randomUUID().toString())
                    .source(map);
            bulkRequest.add(indexRequest);
        }
        try {
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            log.info("response {}", bulkResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
