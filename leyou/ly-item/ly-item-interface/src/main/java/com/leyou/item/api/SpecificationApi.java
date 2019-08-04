package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpecificationApi {
    @GetMapping("/spec/params/search")
    List<SpecParam> queryParamList(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid") Long cid,
            @RequestParam(value = "searching",required = false,defaultValue = "true") Boolean searching);

    // 查询规格参数组
    @GetMapping("/spec/groups/{cid}")
    List<SpecGroup> querySpecGroups(@PathVariable("cid") Long cid);

    // 查询规格参数组，及组内参数
    @GetMapping("/spec/{cid}")
    List<SpecGroup> querySpecsByCid(@PathVariable("cid") Long cid);
}
