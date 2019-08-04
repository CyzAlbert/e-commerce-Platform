package com.leyou.goodsweb.controller;


import com.leyou.goodsweb.service.GoodHtmlService;
import com.leyou.goodsweb.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/item")
public class GoodsController {

    @Autowired
    private GoodService goodService;

    @Autowired
    private GoodHtmlService goodHtmlService;


    /**
     * 返回商品详情spu页面
     * @param model
     * @param id
     * @return
     */
    @GetMapping("{id}.html")
    public String ItemPage(Model model, @PathVariable("id")Long id){
        // 加载所需的数据
        Map<String, Object> modelMap = this.goodService.loadModel(id);
        // 放入模型
        model.addAllAttributes(modelMap);

        // 生成商品静态页面并缓存本地
        goodHtmlService.asyncExcute(id);
        return "item";
    }
}
