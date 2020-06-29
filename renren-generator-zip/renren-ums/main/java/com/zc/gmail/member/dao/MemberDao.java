package com.zc.gmail.member.dao;

import com.zc.gmail.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author zhangchen
 * @email jn.zc@qq.com
 * @date 2020-06-29 11:29:38
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
