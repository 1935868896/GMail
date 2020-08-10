package com.zc.gmail.search.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zc.gmail.search.service.MallSearchService;
import com.zc.gmail.search.vo.SearchParam;
import com.zc.gmail.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @Autowired
    MallSearchService mallSearchService;

    /**
     * 自动将页面提交的数据封装成一个对象
     * @param searchParam
     * @return
     */
    @SentinelResource(value = "sayHello")
    @GetMapping({"/","/list.html"})
    public String indexPage(SearchParam searchParam,Model model){
        SearchResult result = mallSearchService.search(searchParam);
        model.addAttribute("result",result);
        //视图解析器 进行拼接串
        return "list";
    }


}
