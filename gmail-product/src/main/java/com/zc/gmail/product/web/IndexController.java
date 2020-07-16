package com.zc.gmail.product.web;

import com.zc.gmail.product.entity.CategoryEntity;
import com.zc.gmail.product.service.CategoryService;
import com.zc.gmail.product.vo.Catalog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    CategoryService categoryService;

    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){
        List<CategoryEntity> categoryEntities=
                categoryService.getLevel1CateGorys();

        model.addAttribute("categorys",categoryEntities);

        //视图解析器 进行拼接串
   return "index";
    }
    @ResponseBody
    @GetMapping("index/catalog.json")
    public Map<String,List<Catalog2Vo>> getCatalogJson(){
        Map<String, List<Catalog2Vo>> map = categoryService.getCatalogJson();
       return map;
    }

}
