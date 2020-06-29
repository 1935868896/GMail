package com.zc.gmail.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zc.common.utils.PageUtils;
import com.zc.gmail.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author zhangchen
 * @email jn.zc@qq.com
 * @date 2020-06-29 11:32:01
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

