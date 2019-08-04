package com.leyou.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.pojo.Specification;
import com.leyou.mapper.SpecificationMapper;
import com.leyou.service.SpecGroupService;
import com.leyou.service.SpecParamService;
import com.leyou.service.SpecificationService;
import com.leyou.utilpojo.SpecInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private SpecGroupService specGroupService;

    @Autowired
    private SpecParamService specParamService;

    @Override
    public Specification queryById(Long id) {

        return specificationMapper.selectByPrimaryKey(id);
    }

    @Override
    public void saveSpecification(Specification spc) {

        specificationMapper.insert(spc);
    }

    @Override
    public boolean addSpecificationGruop(Long cid, String groupName) {
        Specification spc= specificationMapper.selectByPrimaryKey(cid);
        if(null==spc){
            return false;
        }
        String specInfo=spc.getSpecification();
        Gson gson =new Gson();
        List<SpecInfo> info=gson.fromJson(specInfo, new TypeToken<List<SpecInfo>>(){}.getType());
        SpecInfo oneGroup = new SpecInfo();
        oneGroup.setGroup(groupName);
        info.add(oneGroup);
        String newSpec =gson.toJson(info);
        byte [] bytes=newSpec.getBytes();
        try {
            newSpec = new String(bytes,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        spc.setSpecification(newSpec);
        if(specificationMapper.updateByPrimaryKey(spc)==1){
            return true;
        }else{
            return false;
        }
    }

    /***
     * 根据分类id 查询规格组和规格参数
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroup> querySpecsByCid(Long cid) {
        List<SpecGroup> specGroups =specGroupService.queryGroupById(cid);
        SpecParam specParam=new SpecParam();
        specGroups.forEach(group->{
            group.setParams(specParamService.querySpecParam(group.getId()));
        });
        return specGroups;
    }
}
