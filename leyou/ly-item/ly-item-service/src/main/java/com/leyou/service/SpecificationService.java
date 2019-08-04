package com.leyou.service;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.Specification;

import java.util.List;

public interface SpecificationService {
    Specification queryById(Long id);
    void saveSpecification(Specification spc);
    boolean addSpecificationGruop(Long cid,String groupName);
    List<SpecGroup> querySpecsByCid(Long cid);
}
