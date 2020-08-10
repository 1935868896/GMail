package com.zc.gmail.search.vo;

import lombok.Data;

import java.util.List;

/**
 * 封装传递过来的所有的查询条件
 */

@Data
public class SearchParam {
    private String keyword; //查询关键词
    private Long catalog3Id;
    /**
     * hostScore
     * saleCount;
     * skuPrice
     */
    private String sort;
    /**
     * filter condition
     * hasStock 0/1
     * skuPrice 1-500/_500/500_
     */
    private Integer hasStock =1;  //0(无库存),1(有库存)
    private String skuPrice;
    private List<Long> branIds;
    /**
     * attrs=1_其他:安卓
     * attrs=2_
     */
    private List<String> attrs;

    private Integer pageNum=1;
}
