package com.leyou.search.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.search.LySearchService;
import com.leyou.search.pojo.Goods;
import com.leyou.search.viewobjects.SearchRequest;
import com.leyou.search.viewobjects.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchService.class)
public class SearchServiceTest {
    @Autowired
    private SearchService searchService;

    @Test
    public void test(){
        SearchRequest request =new SearchRequest();
        request.setKey("手机");
        request.setPage(1);
        request.setSortBy("");
        request.setDescending(false);
        PageResult<Goods> res = searchService.search(request);


    }

}
