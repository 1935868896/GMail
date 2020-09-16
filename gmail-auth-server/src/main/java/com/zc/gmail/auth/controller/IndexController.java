package com.zc.gmail.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.zc.common.constant.AuthServerConstant;
import com.zc.common.exception.BizCodeEnume;
import com.zc.common.utils.R;
import com.zc.common.vo.MemberResponseVo;
import com.zc.gmail.auth.feign.MemberFeignService;
import com.zc.gmail.auth.feign.ThirdPartyFeignService;
import com.zc.gmail.auth.vo.UserLoginVo;
import com.zc.gmail.auth.vo.UserRegistVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.jws.WebParam;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
public class IndexController {
    @Autowired
    ThirdPartyFeignService thirdPartyFeignService;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    MemberFeignService memberFeignService;




    @GetMapping({"/","/login.html"})
    public String loginPage(HttpSession session){
   if(session.getAttribute(AuthServerConstant.LOGIN_USER)==null){


        return "login";}else {
       return "redirect:http://localhost:9000/";
   }
    }
    @GetMapping({"/register.html","/reg"})
    public String registerPage(Model model){
        return "register";
    }
    @ResponseBody
    @GetMapping("/sms/sendcode")
    public R sendCode(@RequestParam("phone") String phone){
        //1.接口防刷
        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if(StringUtils.isNotEmpty(redisCode)){
        long l=Long.parseLong(redisCode.split("_")[1]);
        if(System.currentTimeMillis()-l<60*1000){
            //60s内不能再发
            return R.error(BizCodeEnume.VAILD_SMS_EXCPTION.getCode(),BizCodeEnume.VAILD_SMS_EXCPTION.getMsg());
        }}



        //2.验证码的再次校验.选择存到redis  sms:code:phoneNum->code
        String code = UUID.randomUUID().toString().substring(0, 5);
        String subString = code + "_" + System.currentTimeMillis();


        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX+phone,subString,10, TimeUnit.MINUTES);
        //redis缓存验证码,防止同一个phone在60s内再次放松验证码
        thirdPartyFeignService.sendCode(phone,code);
        return R.ok();
    }

    @PostMapping("regist")
    public String regist(@Valid UserRegistVo vo, BindingResult result, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
//            Map<String,String> errors=new HashMap<>();

            /*
            * map(fieldError -> {
                String field=fieldError.getField();
                String msg=fieldError.getDefaultMessage();
                erros.put(field,msg);
                return
            })*/
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(
                    FieldError::getField, fieldError -> {
                        return fieldError.getDefaultMessage();
                    }
            ));
            redirectAttributes.addFlashAttribute("errors",errors);
            //校验出错,转发到注册页
            return "redirect:/reg";
        }
        //真正注册,调用远程服务

        //校验验证码:
        String code = vo.getCode();
        String s=redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX+vo.getPhone());
        if(!StringUtils.isEmpty(s)){
            if(code.equals(s.split("_")[0])){
//                redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX+vo.getPhone());
            //验证码通过
                R r=memberFeignService.regist(vo);
                if(r.getCode()==0){
                    return "redirect:/login.html";
                }else {
                    Map<String,String> errors=new HashMap<>();
                    errors.put("msg",r.getData("msg",new TypeReference<String>(){}));
                    redirectAttributes.addFlashAttribute("errors",errors);


                    return "redirect:reg";
                }
            }
            else {
                Map<String,String> errors=new HashMap<>();
                errors.put("code","验证码错误");
                redirectAttributes.addFlashAttribute("errors",errors);
                return "redirect:reg";
            }
        }else {
            Map<String,String> errors=new HashMap<>();
            errors.put("code","验证码错误");
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:reg";
        }
//        return "redirect:/login.html";
    }
    @PostMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes redirectAttributes, HttpSession session){
       //远程登录
        R login=memberFeignService.login(vo);
       if(login.getCode()==0){
           //success
           MemberResponseVo data=login.getData("data",new TypeReference<MemberResponseVo>(){});
           session.setAttribute(AuthServerConstant.LOGIN_USER,data);
           return "redirect:http://localhost:9000/";
       }else{
           Map<String,String> errors=new HashMap<>();
           errors.put("msg",login.getData("msg",new TypeReference<String>(){}));
           redirectAttributes.addFlashAttribute("errors",errors);
           return "redirect:/login.html";
       }

    }


}
