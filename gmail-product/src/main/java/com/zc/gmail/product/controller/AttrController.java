package com.zc.gmail.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.zc.gmail.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zc.gmail.product.entity.AttrEntity;
import com.zc.gmail.product.service.AttrService;
import com.zc.common.utils.PageUtils;
import com.zc.common.utils.R;



/**
 * 商品属性
 *
 * @author zhangchen
 * @email sjn.zc@qq.com
 * @date 2020-06-29 09:30:54
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

//    @GetMapping("/base/list/{catelogId}")
//    public R baseAttrList(@RequestParam Map<String,Object> params,
//                          @PathVariable("catelogId")Long catelogId){
//        PageUtils pageUtils = attrService.queryBaseAttrPage(params,catelogId);
//        return R.ok().put("page",pageUtils);
//    }
//
@GetMapping("/{attrType}/list/{catelogId}")
public R baseAttrList(@RequestParam Map<String,Object> params,
                      @PathVariable("catelogId")Long catelogId,
                      @PathVariable("attrType")String type){
    PageUtils pageUtils = attrService.queryBaseAttrPage(params,catelogId,type);
    return R.ok().put("page",pageUtils);
}

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
//		AttrEntity attr = attrService.getById(attrId);
        Object respVo = attrService.getAttrInfo(attrId);
        return R.ok().put("attr", respVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo attr){
//		attrService.updateById(attr);
        attrService.updateAttr(attr);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
