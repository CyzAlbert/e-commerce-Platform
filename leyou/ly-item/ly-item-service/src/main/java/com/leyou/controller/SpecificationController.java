package com.leyou.controller;

import com.leyou.common.exception.LyExcption;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.params.Group;
import com.leyou.params.Param;
import com.leyou.service.SpecGroupService;
import com.leyou.service.SpecParamService;
import com.leyou.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spec")
public class SpecificationController {
    @Autowired
    private SpecificationService specificationService;

    @Autowired
    private SpecGroupService specGroupService;

    @Autowired
    private SpecParamService specParamService;

    @GetMapping("/groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecificationGroups(@PathVariable("cid") Long id){
        try {
            List<SpecGroup> groups= specGroupService.queryGroupById(id);
            return ResponseEntity.ok(groups);
        } catch (LyExcption lyExcption) {
            lyExcption.printStackTrace();
            return null;
        }

    }

    @RequestMapping("/group")
    public ResponseEntity<String> saveGroup(@RequestBody Group group){
        /*
        if(specificationService.addSpecificationGruop(group.getCid(),group.getName())){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);*/
        boolean res =specGroupService.addSpecGroup(group.getCid(),group.getName());
        if(res){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/group/{id}")
    public ResponseEntity<String> deleteGroup(@PathVariable("id") Long id){
        boolean res =specGroupService.deleteSpecGroup(id);
        if(res){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> querySpecParam(@RequestParam(name = "gid") Long gid){
        try {
            List<SpecParam> specParams = specParamService.querySpecParam(gid);
            return ResponseEntity.ok(specParams);
        } catch (LyExcption lyExcption) {
            lyExcption.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/param")
    public ResponseEntity<String> saveParam(@RequestBody Param param){
        boolean res =specParamService.saveSpecParam(param);
        if(res){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/param/{id}")
    public ResponseEntity<String> deleteParam(@PathVariable("id") Long id){
        boolean res =specParamService.deleteSpecParam(id);
        if(res){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/category/params")
    public ResponseEntity<List<SpecParam>> queryParamByCid(@RequestParam("cid") Long cid){
        try {
            List<SpecParam> specParams = specParamService.querySpecParamByCid(cid);
            return ResponseEntity.ok(specParams);
        } catch (LyExcption lyExcption) {
            lyExcption.printStackTrace();
            return null;
        }
    }

    /**
     * 提供搜索服务查询规格参数
     * @param gid
     * @param cid
     * @param searching
     * @return
     */
    @GetMapping("/params/search")
    public ResponseEntity<List<SpecParam>> queryParamList(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid") Long cid,
            @RequestParam(value = "searching",required = false,defaultValue = "true") Boolean searching){
        List<SpecParam> specParams = specParamService.queryParamList(gid,cid,searching);
        return ResponseEntity.ok(specParams);
        /*
        if(CollectionUtils.isEmpty(specParams)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            return ResponseEntity.ok(specParams);
        }*/
    }

    /**
     * 为商品详情展示服务提供查询规格参数组和规格参数的接口
     * @param cid
     * @return
     */
    @GetMapping("{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecsByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> list = this.specificationService.querySpecsByCid(cid);
        if(list == null || list.size() == 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(list);
    }

}
