package com.zc.gmail.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.zc.common.valid.AddGroup;
import com.zc.common.valid.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zc.gmail.product.entity.BrandEntity;
import com.zc.gmail.product.service.BrandService;
import com.zc.common.utils.PageUtils;
import com.zc.common.utils.R;

import javax.validation.Valid;


/**
 * 品牌
 *
 * @author zhangchen
 * @email sjn.zc@qq.com
 * @date 2020-06-29 09:30:54
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(/*@Valid*/@Validated(AddGroup.class) @RequestBody BrandEntity brand){//, BindingResult bindingResult
//        if(bindingResult.hasErrors()){
//            Map<String,String> map=new HashMap<>();
//            bindingResult.getFieldErrors().stream().forEach((item)->{
//                map.put(item.getField(),item.getDefaultMessage());
//
//            });
//          R.error(400,"提交的数据不合法").put("data",map);
//        }else{
//
//        }
		brandService.save(brand);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@Validated(UpdateGroup.class)@RequestBody BrandEntity brand){
		brandService.updateDetail(brand);
        brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
