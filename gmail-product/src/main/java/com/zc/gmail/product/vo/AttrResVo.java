package com.zc.gmail.product.vo;

import lombok.Data;

@Data
public class AttrResVo extends AttrVo {
    private String catelogName;
    private String groupName;
    private Long[] catelogPath;
}
