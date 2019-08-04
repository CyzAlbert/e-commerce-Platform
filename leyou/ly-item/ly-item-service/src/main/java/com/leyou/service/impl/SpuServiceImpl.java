package com.leyou.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.extendpojo.SpuExt;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.mapper.BrandMapper;
import com.leyou.mapper.SpuDetailMapper;
import com.leyou.mapper.SpuMapper;
import com.leyou.service.CategoryService;
import com.leyou.service.SpuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Override
    public PageResult<SpuExt> querySpuPage(Integer page, Integer rows, Boolean saleable,String key) {
        PageHelper.startPage(page,Math.min(rows,100));
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(saleable!=null){
            criteria.orEqualTo("saleable",saleable?1:0);
        }
        if(!StringUtils.isEmpty(key)){
            criteria.andLike("title","%" + key + "%");
        }
        Page<Spu> pageInfo= (Page<Spu>)spuMapper.selectByExample(example);
        List<SpuExt> res= pageInfo.getResult().stream().map(spu -> {
            // spu -> spuExt
            SpuExt spuEx=new SpuExt();
            BeanUtils.copyProperties(spu,spuEx);
            List<String> categoryNames=categoryService.getCategoryByIds(
                    Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
            spuEx.setCname(String.join("/",categoryNames));
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuEx.setBname(brand.getName());
            return spuEx;
        }).collect(Collectors.toList());
        return new PageResult<>(pageInfo.getTotal(), res);
    }

    @Override
    public SpuDetail querySpuDetailById(Long spuId) {
        return spuDetailMapper.selectByPrimaryKey(spuId);
    }
}
