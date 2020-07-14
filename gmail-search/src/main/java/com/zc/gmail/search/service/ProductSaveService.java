package com.zc.gmail.search.service;

import com.zc.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

public interface ProductSaveService {

    public Boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
