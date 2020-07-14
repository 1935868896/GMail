package com.zc.gmail.search;

import com.alibaba.fastjson.JSON;
import com.zc.gmail.search.config.GmailElasticSearchConfig;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class GmailSearchApplicationTests {
    @Autowired
    RestHighLevelClient client;


    @Test
    void contextLoads() {
    }

    @Test
    public void search(){
        System.out.println(client);

    }

    /**
     * 测试存储数据到es
     */
    @Test
    public void indexData() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("3");
        User user = new User();
        String jsonString = JSON.toJSONString(user);
        indexRequest.source(jsonString, XContentType.JSON);
        IndexResponse index = client.index(indexRequest, GmailElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(index);
    }

    @Test
    public void searchData() throws IOException {
        System.out.printf("start");
        SearchRequest searchRequest = new SearchRequest();
        //指定索引
        searchRequest.indices("bank");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        TermsAggregationBuilder Ageagg = AggregationBuilders.terms("ageAgg").field("age").size(10);
        AvgAggregationBuilder balanceagg = AggregationBuilders.avg("balanceAgg").field("balance");
        searchSourceBuilder.aggregation(Ageagg);
        searchSourceBuilder.aggregation(balanceagg);
        //指定检索条件
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest, GmailElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(search.toString());
        SearchHits totalHits = search.getHits();
        SearchHit[] hits = totalHits.getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            Account account = JSON.parseObject(sourceAsString, Account.class);
            System.out.println("account:"+account.toString());
        }
        Aggregations aggregations = search.getAggregations();
        Terms ageAgg = aggregations.get("ageAgg");

        System.out.printf("end");
    }

    @Data
    class User{
        String userName;
        Integer userId;
    }
    @Data
    @ToString
    static class Account{
        private int account_number;

        private int balance;

        private String firstname;

        private String lastname;

        private int age;

        private String gender;

        private String address;

        private String employer;

        private String email;

        private String city;

        private String state;

    }
}
