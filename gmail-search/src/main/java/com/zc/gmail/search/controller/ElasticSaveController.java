package com.zc.gmail.search.controller;

import com.zc.common.exception.BizCodeEnume;
import com.zc.common.to.es.SkuEsModel;
import com.zc.common.utils.R;
import com.zc.gmail.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/search")
@RestController
@Slf4j
public class ElasticSaveController {

   @Autowired
   ProductSaveService productSaveService;

   @PostMapping("/save/product")
    public R  productStatusUp(@RequestBody List<SkuEsModel> skuEsModel){
        boolean b=true;
        try {
           b= productSaveService.productStatusUp(skuEsModel);
        }catch (Exception e){
            log.error("商品上架错误",e);
         return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());
        }
         if(!b){
             return R.ok();
         }else {
             return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());

         }


    }
}
