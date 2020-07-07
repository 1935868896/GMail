package com.zc.gmail.product;

//import com.aliyun.oss.OSS;
//import com.aliyun.oss.OSSClientBuilder;
import com.zc.gmail.product.entity.BrandEntity;
import com.zc.gmail.product.service.BrandService;
import com.zc.gmail.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class GmailProductApplicationTests {
    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;



    @Test
    void categoryTest(){
        Long[] ll = categoryService.findCatelogPath((long) 225);
        System.out.printf("完整路径");
    }


    @Test
    void contextLoads() {
        BrandEntity brandEntity=new BrandEntity();
        brandEntity.setName("华为");
        brandService.save(brandEntity);
        System.out.println("保存成功");
    }

}
