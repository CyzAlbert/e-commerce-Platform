package com.leyou.service.impl;

import com.leyou.item.pojo.Category;
import com.leyou.mapper.CategoryMapper;
import com.leyou.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getCategoryListByParentId(Long pid) {
        Category category =new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    @Override
    public List<String> getCategoryByIds(List<Long> ids) {
        return categoryMapper.selectByIdList(ids).stream().map(category -> {
            return category.getName();
        }).collect(Collectors.toList());
    }

    @Override
    public List<Category> queryAllByCid3(Long cid3) {
        Category c3 = this.categoryMapper.selectByPrimaryKey(cid3);
        Category c2 = this.categoryMapper.selectByPrimaryKey(c3.getParentId());
        Category c1 = this.categoryMapper.selectByPrimaryKey(c2.getParentId());
        return Arrays.asList(c1,c2,c3);
    }
}
