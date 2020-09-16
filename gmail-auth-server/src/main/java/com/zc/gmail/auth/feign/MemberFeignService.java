package com.zc.gmail.auth.feign;

import com.zc.common.utils.R;
import com.zc.gmail.auth.vo.SocialUser;
import com.zc.gmail.auth.vo.UserLoginVo;
import com.zc.gmail.auth.vo.UserRegistVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gmail-member")
public interface MemberFeignService {

    @PostMapping("/member/member/regist")
    R regist(@RequestBody UserRegistVo vo);

    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo vo);

    @PostMapping("/member/member/auth2/login")
    public R oauthlogin(@RequestBody SocialUser vo);
}
