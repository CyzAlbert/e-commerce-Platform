package com.leyou.service;

import com.leyou.item.pojo.SpecParam;
import com.leyou.params.Param;

import java.util.List;

public interface SpecParamService {
    List<SpecParam> querySpecParam(Long gid);
    boolean saveSpecParam(Param param);
    boolean deleteSpecParam(Long id);
    List<SpecParam> querySpecParamByCid(Long cid);
    List<SpecParam> queryParamList(Long gid,Long cid,Boolean searching);
}
