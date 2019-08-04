package com.leyou.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

import java.util.List;

public interface BrandService {
    PageResult<Brand> getPageBrand(Integer page,Integer rows,String sortBy,Boolean desc,String key);
    void saveBrand(Brand brand, List<Long> cids);
    List<Brand> queryBrandsByCid(Long cid);
    Brand queryById(Long id);
    List<Brand> queryBrandsByIds(List<Long> ids);
}
