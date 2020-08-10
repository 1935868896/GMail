package com.zc.gmail.search.vo;

import com.zc.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;
@Data
public class SearchResult {

    private List<SkuEsModel> products;


    /**
     * 分页信息
     */
    private Integer pageNum;

    private Long totalRecords;

    private Integer totalPages;

    private List<Integer> pageNavs;

    private List<BrandVO> brands; //查询出的结果所有涉及到的品牌
    private List<CatalogVO> catalogs;
    private List<AttrVO> attrs;

    @Data
    public static class BrandVO{
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    public static class AttrVO{
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }

    @Data
    public static class CatalogVO{
        private Long catalogId;
        private String catalogName;
    }


}
