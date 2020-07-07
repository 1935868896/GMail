package com.zc.gmail.product.exception;

import com.zc.common.exception.BizCodeEnume;
import com.zc.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sun.rmi.runtime.Log;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.zc.gmail.product.controller")
//@ControllerAdvice(basePackages = "com.zc.gmail.product.controller")
public class GmailExceptionControllerAdvice {


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult=e.getBindingResult();
        log.error("数据校验出现问题{},异常类型{}",e.getMessage(),e.getClass());
        Map<String,String> map=new HashMap<>();
            bindingResult.getFieldErrors().stream().forEach((item)->{
                map.put(item.getField(),item.getDefaultMessage());

            });
        return R.error(BizCodeEnume.VAILD_EXCPTION.getCode(),BizCodeEnume.VAILD_EXCPTION.getMsg()).put("data",map);
    }
    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){
        throwable.printStackTrace();
        return R.error(BizCodeEnume.UNKONW_EXCEPTION.getCode(),BizCodeEnume.UNKONW_EXCEPTION.getMsg());

    }
}
