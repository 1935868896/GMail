package com.zc.gmail.coupon.dao;

import com.zc.gmail.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author zhangchen
 * @email jn.zc@qq.com
 * @date 2020-06-29 11:26:57
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
