package com.zc.gmail.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {
    @GetMapping({"/","/login.html"})
    public String loginPage(Model model){
//        List<CategoryEntity> categoryEntities=
//                categoryService.getLevel1CateGorys();
//
//        model.addAttribute("categorys",categoryEntities);

        //视图解析器 进行拼接串
        return "login";
    }
    @GetMapping({"/register.html"})
    public String registerPage(Model model){
//        List<CategoryEntity> categoryEntities=
//                categoryService.getLevel1CateGorys();
//
//        model.addAttribute("categorys",categoryEntities);

        //视图解析器 进行拼接串
        return "register";
    }

}
