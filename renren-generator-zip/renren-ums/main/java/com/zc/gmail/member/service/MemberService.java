package com.zc.gmail.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zc.common.utils.PageUtils;
import com.zc.gmail.member.entity.MemberEntity;

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
}

