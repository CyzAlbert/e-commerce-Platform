package com.leyou.service.impl;

import com.leyou.common.exception.ExceptionEnum;
import com.leyou.common.exception.LyExcption;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.mapper.SpecGroupMapper;
import com.leyou.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.swing.table.TableRowSorter;
import java.util.List;

@Service
public class SpecGroupServiceImpl implements SpecGroupService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Override
    public List<SpecGroup> queryGroupById(Long cid) throws LyExcption{
        SpecGroup specGroup=new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> specGroups = specGroupMapper.select(specGroup);
        if(CollectionUtils.isEmpty(specGroups)){
            throw new LyExcption(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
        return specGroups;
    }

    @Override
    public boolean addSpecGroup(Long cid, String name) {
        SpecGroup specGroup=new SpecGroup();
        specGroup.setCid(cid);
        specGroup.setName(name);
        int res=specGroupMapper.insert(specGroup);
        if(res==0){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public boolean deleteSpecGroup(Long id) {
        int res=specGroupMapper.deleteByPrimaryKey(id);
        if(res==1) {
            return true;
        }else{
            return false;
        }
    }
}
