package com.zc.gmail.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.zc.common.exception.BizCodeEnume;
import com.zc.gmail.member.exception.PhoneExistException;
import com.zc.gmail.member.exception.UserNameExistException;
import com.zc.gmail.member.feign.CouponFeginService;
import com.zc.gmail.member.vo.MemberLoginVo;
import com.zc.gmail.member.vo.RegisterVo;
import com.zc.gmail.member.vo.SocialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zc.gmail.member.entity.MemberEntity;
import com.zc.gmail.member.service.MemberService;
import com.zc.common.utils.PageUtils;
import com.zc.common.utils.R;



/**
 * 会员
 *
 * @author zhangchen
 * @email jn.zc@qq.com
 * @date 2020-06-29 11:29:38
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    CouponFeginService couponFeginService;
    @RequestMapping("test")
    public R test(){
        MemberEntity memberEntity=new MemberEntity();
        memberEntity.setNickname("张三");
        R membercoupons=couponFeginService.membercoupons();
        return R.ok().put("member",memberEntity).put("coupons",membercoupons.get("coupons"));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }

    @PostMapping("/auth2/login")
    public R oauthlogin(@RequestBody SocialUser vo) throws Exception {
        MemberEntity memberEntity=memberService.login(vo);
        if(memberEntity!=null){
            return R.ok().setData(memberEntity);
        }else {
            return R.error(BizCodeEnume.LOGIN_PASSWORD_INVALD_EXCEPTION.getCode(),BizCodeEnume.LOGIN_PASSWORD_INVALD_EXCEPTION.getMsg());
        }


    }
    @PostMapping("/login")
    public R login(@RequestBody MemberLoginVo vo){
        MemberEntity memberEntity=memberService.login(vo);
        if(memberEntity!=null){
            return R.ok().setData(memberEntity);
        }else {
            return R.error(BizCodeEnume.LOGIN_PASSWORD_INVALD_EXCEPTION.getCode(),BizCodeEnume.LOGIN_PASSWORD_INVALD_EXCEPTION.getMsg());
        }


    }

    @PostMapping("/regist")
    public R regist(@RequestBody RegisterVo vo){
        try {
            memberService.regist(vo);
        }catch (PhoneExistException e){
            return R.error(BizCodeEnume.PHONE_EXIST_EXCEPTION.getCode(),BizCodeEnume.PHONE_EXIST_EXCEPTION.getMsg());

        }catch (UserNameExistException e) {
            return R.error(BizCodeEnume.USER_EXIST_EXCEPTION.getCode(),BizCodeEnume.USER_EXIST_EXCEPTION.getMsg());
        }

        return R.ok();
    }
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
