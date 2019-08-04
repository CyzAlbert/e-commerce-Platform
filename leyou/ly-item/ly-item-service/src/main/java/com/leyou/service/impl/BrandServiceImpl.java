package com.leyou.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.mapper.BrandMapper;
import com.leyou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageResult<Brand> getPageBrand(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        PageHelper.startPage(page,rows);
        Example example =new Example(Brand.class);
        if(!StringUtils.isEmpty(key)) {
            example.createCriteria().andLike("name", "%" + key + "%").orEqualTo("letter", key);
        }
        if(!StringUtils.isEmpty(sortBy)){
            String orderBy=sortBy+(desc?" DESC":" ASC");
            example.setOrderByClause(orderBy);
        }
        Page<Brand> pageInfo =(Page<Brand>) brandMapper.selectByExample(example);

        return new PageResult<Brand>(pageInfo.getTotal(),pageInfo.getResult());
    }

    @Override
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        brandMapper.insertSelective(brand);
        for(Long cid:cids){
            brandMapper.insertCategoryBrand(cid,brand.getId());
        }
    }

    @Override
    public List<Brand> queryBrandsByCid(Long cid) {
        List<Brand> brands = brandMapper.queryBrandsByCid(cid);
        if(CollectionUtils.isEmpty(brands)){
            return null;
        }
        return brands;
    }

    @Override
    public Brand queryById(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Brand> queryBrandsByIds(List<Long> ids) {
        return brandMapper.selectByIdList(ids);
    }
}
