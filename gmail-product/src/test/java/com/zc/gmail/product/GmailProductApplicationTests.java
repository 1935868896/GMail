package com.zc.gmail.product;

//import com.aliyun.oss.OSS;
//import com.aliyun.oss.OSSClientBuilder;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zc.common.utils.Query;
import com.zc.gmail.product.dao.BrandDao;
import com.zc.gmail.product.dao.CategoryDao;
import com.zc.gmail.product.entity.BrandEntity;
import com.zc.gmail.product.entity.CategoryEntity;
import com.zc.gmail.product.service.BrandService;
import com.zc.gmail.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class GmailProductApplicationTests {
    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    BrandDao brandDao;
    @Autowired
    CategoryDao categoryDao;

    @Test
    void mybatisPlusTest() {
        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("name","华为");
        List<BrandEntity> brandEntities = brandDao.selectList(wrapper);
        System.out.printf("end");

    }

    @Test
    void mybatisPage(){
         Map<String, Object> params=new HashMap<>();
         params.put("page","1");
         params.put("limit","5");
        IPage<BrandEntity> page1 = new Query<BrandEntity>().getPage(params);
        IPage<BrandEntity> page = brandService.page(
                page1,
                new QueryWrapper<BrandEntity>()
        );
        System.out.printf("end");
    }

    @Test
    void categoryTree() {
        List<CategoryEntity> entites = categoryService.list();

        List<CategoryEntity> collect = getChild(entites.get(1),entites);
        List<CategoryEntity> collect1 = entites.stream().filter((entity) -> {
            return entity.getParentCid() == 0;
        }).map((entity) -> {
            entity.setChildren(getChild(entity, entites));
            return entity;
        }).collect(Collectors.toList());
        System.out.printf("完整路径");
    }
    List<CategoryEntity> getChild(CategoryEntity root,List<CategoryEntity> all){
        //根据id查询出自己的
        List<CategoryEntity> collect = all.stream().filter((entity)->{
            return  entity.getParentCid()==root.getCatId();
        }).map((entity) -> {
             entity.setChildren(getChild(entity,all));
             return entity;
        }).collect(Collectors.toList());
        return collect;
    }

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("华为");
        brandService.save(brandEntity);
        System.out.println("保存成功");
    }

}
