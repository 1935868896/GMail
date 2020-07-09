package com.zc.gmail.product.feign;

import com.zc.common.to.SkuReductionTo;
import com.zc.common.to.SpuBoundTo;
import com.zc.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gmail-coupon")
public interface CouponFeignService {
    /**
     * 1.将对象转为json
     * 2.找到gmail-coupon服务,给url发送请求,将上一步转成的json放在请求体
     * 3.对方服务收到请求,请求体里有json数据
     * 只要json数据模型是兼容的,双方服务无需使用同一个to
     * @param spuBoundTo
     * @return
     */
    @PostMapping("coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
