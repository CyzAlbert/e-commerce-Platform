package com.leyou.service;

import com.leyou.common.exception.LyExcption;
import com.leyou.item.pojo.SpecGroup;

import java.util.List;

public interface SpecGroupService {
    List<SpecGroup> queryGroupById(Long cid) throws LyExcption;
    boolean addSpecGroup(Long cid,String name);
    boolean deleteSpecGroup(Long id);
}
