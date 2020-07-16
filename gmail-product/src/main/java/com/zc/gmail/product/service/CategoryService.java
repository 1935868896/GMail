package com.zc.gmail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zc.common.utils.PageUtils;
import com.zc.gmail.product.entity.CategoryEntity;
import com.zc.gmail.product.vo.Catalog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author zhangchen
 * @email sjn.zc@qq.com
 * @date 2020-06-29 09:30:54
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenusByIds(List<Long> asList);

    Long[] findCatelogPath(Long catelogId);

    void updateCascade(CategoryEntity category);

    List<CategoryEntity> getLevel1CateGorys();

    Map<String,  List<Catalog2Vo>> getCatalogJson();
}

