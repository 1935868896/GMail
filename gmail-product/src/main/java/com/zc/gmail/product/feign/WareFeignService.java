package com.zc.gmail.product.feign;

import com.zc.common.utils.R;
import com.zc.gmail.product.vo.SkuHasStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gmail-ware")
public interface WareFeignService {
    /**
     * 1.R设计的时候可以加上泛型
     * @param skuIds
     * @return
     */
    @PostMapping("/ware/waresku/hasstock")
    R getSkuHasStock(@RequestBody List<Long> skuIds);
}
