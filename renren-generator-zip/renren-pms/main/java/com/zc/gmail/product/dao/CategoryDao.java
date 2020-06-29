package com.zc.gmail.product.dao;

import com.zc.gmail.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author zhangchen
 * @email sjn.zc@qq.com
 * @date 2020-06-29 09:30:54
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
