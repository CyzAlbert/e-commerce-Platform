package com.leyou.service.impl;

import com.leyou.common.exception.ExceptionEnum;
import com.leyou.common.exception.LyExcption;
import com.leyou.item.pojo.SpecParam;
import com.leyou.mapper.SpecParamMapper;
import com.leyou.params.Param;
import com.leyou.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SpecParamServiceImpl implements SpecParamService {

    @Autowired
    private SpecParamMapper specParamMapper;

    @Override
    public List<SpecParam> querySpecParam(Long gid) {
        SpecParam specParam=new SpecParam();
        specParam.setGroupId(gid);
        List<SpecParam> specParams = specParamMapper.select(specParam);
        if(CollectionUtils.isEmpty(specParams)){
            throw new LyExcption(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        return specParams;
    }

    @Override
    public boolean saveSpecParam(Param param) {
        SpecParam specParam =new SpecParam();
        specParam.setGroupId(param.getGroupId());
        specParam.setCid(param.getCid());
        specParam.setName(param.getName());
        specParam.setGeneric(param.isGeneric()?1:0);
        specParam.setIsNum(param.isNumeric()?1:0);
        specParam.setUnit(param.getUnit());
        specParam.setSearching(param.isSearching()?1:0);
        specParam.setSegments(param.getSegments());
        int res = specParamMapper.insert(specParam);
        if (res == 1) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean deleteSpecParam(Long id) {
        int res=specParamMapper.deleteByPrimaryKey(id);
        if(res==1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<SpecParam> querySpecParamByCid(Long cid) {
        SpecParam specParam=new SpecParam();
        specParam.setCid(cid);
        List<SpecParam> specParams = specParamMapper.select(specParam);
        if(CollectionUtils.isEmpty(specParams)){
            throw new LyExcption(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        return specParams;
    }

    @Override
    public List<SpecParam> queryParamList(Long gid, Long cid, Boolean searching) {
        SpecParam specParam=new SpecParam();
        specParam.setCid(cid);
        specParam.setGroupId(gid);
        specParam.setSearching(searching?1:0);
        List<SpecParam> specParams =specParamMapper.select(specParam);
        return  specParams;
    }


}
