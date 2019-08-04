package com.leyou.controller;

import com.leyou.item.pojo.Category;
import com.leyou.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryCategoryList(
            @RequestParam(value = "pid",defaultValue = "0") Long pid){
        try{
            if(null==pid || pid.longValue()<0){
                return ResponseEntity.badRequest().build();
            }
            List<Category> categoryList = categoryService.getCategoryListByParentId(pid);
            if(CollectionUtils.isEmpty(categoryList)){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(categoryList);
        }catch (Exception e){
            logger.error("query failed ........");
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 根据id 查询分类
     * @param ids
     * @return
     */
    @GetMapping("/list/ids")
    public ResponseEntity<List<String>> queryCategoryByIds(@RequestParam("ids")List<Long> ids){
        List<String> categoryList = categoryService.getCategoryByIds(ids);
        if(!CollectionUtils.isEmpty(categoryList)){
            return ResponseEntity.ok(categoryList);
        } else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /***
     * 根据category cid3 查询所有商品1-3级分类
     * @param id
     * @return
     */
    @GetMapping("/all/level")
    public ResponseEntity<List<Category>> queryAllByCid3(@RequestParam("id") Long id){
        List<Category> categories = categoryService.queryAllByCid3(id);
        if(CollectionUtils.isEmpty(categories)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(categories);
    }
}
