package com.zc.gmail.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.zc.common.to.es.SkuEsModel;
import com.zc.gmail.search.config.GmailElasticSearchConfig;
import com.zc.gmail.search.constant.EsConstant;
import com.zc.gmail.search.service.MallSearchService;
import com.zc.gmail.search.vo.SearchParam;
import com.zc.gmail.search.vo.SearchResult;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MallSearchServiceImpl implements MallSearchService {
    @Autowired
    private RestHighLevelClient client;
    @Override
    public SearchResult search(SearchParam param) {
        SearchRequest searchRequest = buildSearchRequest(param);
        SearchResult searchResult=null;
        try {
            //执行检索请求
            SearchResponse response=client.search(searchRequest, GmailElasticSearchConfig.COMMON_OPTIONS);
            //分析响应数据封装成我们需要的格式
           searchResult  = buildSearchResult(response, param);
        }catch (Exception e){

        }

        return searchResult;
    }

    private SearchRequest buildSearchRequest(SearchParam param) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        /**
         * 模糊匹配,过滤(按照属性,分类,品牌,价格区间,库存)
         */
        //1.构建boolQuery
        BoolQueryBuilder boolQueryBuilder =QueryBuilders.boolQuery();
        sourceBuilder.query(boolQueryBuilder);
        //1.1.bool--must 模糊匹配
        if(!StringUtils.isEmpty(param.getKeyword())){
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle",param.getKeyword()));
        }
        //1.2.bool--filter 按照三级分类id查询
        if(param.getCatalog3Id()!=null){
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId",param.getCatalog3Id()));
        }
        //1.2 品牌id
        if(param.getBranIds()!=null&& param.getBranIds().size()>0){
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId",param.getBranIds()));
        }
        //1.2 属性
        if(param.getAttrs()!=null&&param.getAttrs().size()>0){
            BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
            for (String attr : param.getAttrs()) {
                String[] s=attr.split("_");
                String attrId=s[0];
                String[] attrValues=s[1].split(":");
                nestedBoolQueryBuilder.must(QueryBuilders.termQuery("attrs.attrId",attrId));
                nestedBoolQueryBuilder.must(QueryBuilders.termsQuery("attrs.attrValue",attrValues));
                NestedQueryBuilder attrsQuery = QueryBuilders.nestedQuery("attrs", nestedBoolQueryBuilder, ScoreMode.None);
                boolQueryBuilder.filter(attrsQuery);
            }

            
            
        }
        //1.2 是否拥有库存
        if(param.getHasStock()==1) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock", param.getHasStock() == 1));
        }
        //1.2 按照价格区间
        if(!StringUtils.isEmpty(param.getSkuPrice())){
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
             String[] s=param.getSkuPrice().split("_");
             if(s.length==2){
                 rangeQuery.gte(s[0]).lte(s[1]);
             }else if(s.length==1){
                 if(param.getSkuPrice().startsWith("_")){
                     rangeQuery.lte(s[0]);
                 }else {
                     rangeQuery.gte(s[0]);
                 }
             }
             boolQueryBuilder.filter(rangeQuery);
        }

        sourceBuilder.query(boolQueryBuilder);

        /**
         * 排序,分页,聚合分析
         */
         //排序
        if(!StringUtils.isEmpty(param.getSort())){
            String sort=param.getSort();
            String[] s=sort.split("_");
            SortOrder order=s[1].equalsIgnoreCase("asc")?SortOrder.ASC:SortOrder.DESC;
            sourceBuilder.sort(s[0],order);
        }
        //分页
        sourceBuilder.from((param.getPageNum()-1)*EsConstant.PRODUCT_PAGESIZE);
        sourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);

        //高亮
        if(!StringUtils.isEmpty(param.getKeyword())){
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            sourceBuilder.highlighter(highlightBuilder);
        }
        AggregationBuilders.terms("brand_agg");
        /**
         * 聚合分析
         */
        // 品牌聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg").field("brandId").size(10);
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        sourceBuilder.aggregation(brand_agg);

        // 分类聚合
        TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg").field("catalogId").size(20);
        catalog_agg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));
        sourceBuilder.aggregation(catalog_agg);

        // 属性聚合
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));
        attr_agg.subAggregation(attr_id_agg);
        sourceBuilder.aggregation(attr_agg);


        String s = sourceBuilder.toString();
        System.out.printf(s);
        SearchRequest searchResult=new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX},sourceBuilder);

        return searchResult;
    }

    /**
     * 构建结果数据
     *
     * @param response
     * @param searchParam
     * @return
     */
    private SearchResult buildSearchResult(SearchResponse response, SearchParam searchParam) {
        SearchResult result = new SearchResult();
        SearchHits hits = response.getHits();

        // 设置 products
        List<SkuEsModel> esSkuModels = new ArrayList<>();
        if (hits.getHits() != null && hits.getHits().length > 0) {
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel esSkuModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                if (!StringUtils.isEmpty(searchParam.getKeyword())) {
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    esSkuModel.setSkuTitle(skuTitle.fragments()[0].string());
                }
                esSkuModels.add(esSkuModel);
            }
        }
        result.setProducts(esSkuModels);

        // 设置聚合信息

        // 设置属性聚合信息
        List<SearchResult.AttrVO> attrVOS = new ArrayList<>();
        ParsedNested attr_agg = response.getAggregations().get("attr_agg");
        ParsedLongTerms attr_id_agg = attr_agg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attr_id_agg.getBuckets()) {
            SearchResult.AttrVO attrVO = new SearchResult.AttrVO();
            attrVO.setAttrId(bucket.getKeyAsNumber().longValue());
            String attrName = ((ParsedStringTerms) bucket.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();
            List<String> attrValues = ((ParsedStringTerms) bucket.getAggregations().get("attr_value_agg"))
                    .getBuckets().stream().map(MultiBucketsAggregation.Bucket::getKeyAsString).collect(Collectors.toList());
            attrVO.setAttrName(attrName);
            attrVO.setAttrValue(attrValues);
            attrVOS.add(attrVO);
        }
        result.setAttrs(attrVOS);

        // 设置品牌聚合信息
        List<SearchResult.BrandVO> brandVOS = new ArrayList<>();
        ParsedLongTerms brand_agg = response.getAggregations().get("brand_agg");
        for (Terms.Bucket bucket : brand_agg.getBuckets()) {
            SearchResult.BrandVO brandVO = new SearchResult.BrandVO();
            brandVO.setBrandId(bucket.getKeyAsNumber().longValue());
            String brand_name_agg = ((ParsedStringTerms) bucket.getAggregations().get("brand_name_agg")).getBuckets().get(0).getKeyAsString();
            brandVO.setBrandName(brand_name_agg);
            String brand_img_agg = ((ParsedStringTerms) bucket.getAggregations().get("brand_img_agg")).getBuckets().get(0).getKeyAsString();
            brandVO.setBrandImg(brand_img_agg);
            brandVOS.add(brandVO);
        }
        result.setBrands(brandVOS);

        // 设置分类聚合信息
        ParsedLongTerms catalog_agg = response.getAggregations().get("catalog_agg");
        List<SearchResult.CatalogVO> catalogVOS = new ArrayList<>();
        List<? extends Terms.Bucket> buckets = catalog_agg.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            SearchResult.CatalogVO catalogVO = new SearchResult.CatalogVO();
            // 获取分类 id
            String catalogIdString = bucket.getKeyAsString();
            catalogVO.setCatalogId(Long.parseLong(catalogIdString));

            // 获取分类名
            ParsedStringTerms catalog_name_agg = bucket.getAggregations().get("catalog_name_agg");
            String catalogNameString = catalog_name_agg.getBuckets().get(0).getKeyAsString();
            catalogVO.setCatalogName(catalogNameString);
            catalogVOS.add(catalogVO);
        }
        result.setCatalogs(catalogVOS);

        // 设置分页信息
        result.setPageNum(searchParam.getPageNum());
        long totalRecords = hits.getTotalHits().value;
        result.setTotalRecords(totalRecords);
        int totalPages = totalRecords % EsConstant.PRODUCT_PAGESIZE == 0 ? (int) totalRecords / EsConstant.PRODUCT_PAGESIZE : (int) totalRecords /EsConstant.PRODUCT_PAGESIZE + 1;
        result.setTotalPages(totalPages);

        List<Integer> pageNavs = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNavs.add(i);
        }
        result.setPageNavs(pageNavs);

        return result;
    }
}
