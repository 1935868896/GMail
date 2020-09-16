package com.zc.gmail.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zc.common.utils.PageUtils;
import com.zc.gmail.member.entity.MemberEntity;
import com.zc.gmail.member.exception.PhoneExistException;
import com.zc.gmail.member.exception.UserNameExistException;
import com.zc.gmail.member.vo.MemberLoginVo;
import com.zc.gmail.member.vo.RegisterVo;
import com.zc.gmail.member.vo.SocialUser;

import java.util.Map;

/**
 * 会员
 *
 * @author zhangchen
 * @email jn.zc@qq.com
 * @date 2020-06-29 11:29:38
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void regist(RegisterVo vo);

    void checkPhoneUnique(String phone) throws PhoneExistException;
    void checkUserNameUnique(String userName) throws UserNameExistException;

    MemberEntity login(MemberLoginVo vo);

    MemberEntity login(SocialUser vo) throws Exception;
}

