package com.zc.gmail.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zc.common.utils.HttpUtils;
import com.zc.common.utils.R;
import com.zc.gmail.auth.feign.MemberFeignService;
import com.zc.common.vo.MemberResponseVo;
import com.zc.gmail.auth.vo.SocialUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Controller
public class OAuth2Controller {

    @Autowired
    MemberFeignService memberFeignService;


    @GetMapping("/oauth2.0/weibo/success")
    public String weibo(@RequestParam("code") String code, HttpSession session) throws Exception {
        //https://api.weibo.com/oauth2/access_token?client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET&grant_type=authorization_code&redirect_uri=YOUR_REGISTERED_REDIRECT_URI&code=CODE
        Map<String, String> map = new HashMap<>();
        map.put("client_id", "862481048");
        map.put("client_secret", "50268fd0546b3ba218c401f9df351337");
        map.put("grant_type", "authorization_code");
        map.put("redirect_uri", "http://127.0.0.1:9009/oauth2.0/weibo/success");
        map.put("code", code);

        HttpResponse response = HttpUtils.doPost("https://api.weibo.com", "/oauth2/access_token", "post", new HashMap<>(),  map,new HashMap<>());
        //处理
        if (response.getStatusLine().getStatusCode() == 200) {
            String json = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);

            //知道是当前哪个用户
            //1.当前用户如果第一次进入,需要我们首先注册(为当前社交用户生成一个会员信息账号)
            R oauthlogin = memberFeignService.oauthlogin(socialUser);
            if (oauthlogin.getCode() == 0) {
                MemberResponseVo data = oauthlogin.getData("data", new TypeReference<MemberResponseVo>() {
                });
              log.info("登陆成功:用户信息为:"+data.toString());
              session.setAttribute("loginUser",data);
                return "redirect:http://127.0.0.1:9000";
            } else {

                return "redirect:/";
            }

        } else {
            return "redirect:/";
        }

    }
}
