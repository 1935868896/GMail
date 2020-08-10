package com.zc.gmail.search.service;

import com.zc.gmail.search.vo.SearchParam;
import com.zc.gmail.search.vo.SearchResult;

public interface MallSearchService {
    SearchResult search(SearchParam param);
}
