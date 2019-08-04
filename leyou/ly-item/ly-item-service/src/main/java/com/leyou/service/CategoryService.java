package com.leyou.service;

import com.leyou.item.pojo.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getCategoryListByParentId(Long pid);
    List<String> getCategoryByIds(List<Long> ids);
    List<Category> queryAllByCid3(Long cid3);
}
