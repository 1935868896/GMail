package com.zc.gmail.member.feign;

import com.zc.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

//编写一个接口,接口中的每个方法都是调用远程连接的请求

@FeignClient("gmail-coupon")
public interface CouponFeginService {


    @RequestMapping("/coupon/coupon/member/list")
    public R membercoupons();


}