package com.leyou.search.viewobjects;

import java.util.Iterator;
import java.util.Map;

public class SearchRequest {
    // 搜索条件
    private String key;
    // 当前页
    private Integer page;

    // 排序字段
    private String sortBy;

    // 是否降序
    private Boolean descending;

    // 参数过滤条件
    private Map<String,String> filter;

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getDescending() {
        return descending;
    }

    public void setDescending(Boolean descending) {
        this.descending = descending;
    }

    public static Integer getSize() {
        return DEFAULT_SIZE;
    }

    // 每页大小，不从页面接收，而是固定大小
    private static final Integer DEFAULT_SIZE = 20;
    // 默认页
    private static final Integer DEFAULT_PAGE = 1;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if(null==page){
            return DEFAULT_PAGE;
        }
        return Math.max(DEFAULT_PAGE,page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        StringBuilder str =new StringBuilder();
        str.append("key:"+this.key+"\n");
        str.append("page:"+this.page+"\n");
        str.append("sortBy:"+this.sortBy+"\n");
        str.append("descending:"+this.descending+"\n");
        str.append("filter:"+"\n");
        Iterator<Map.Entry<String, String>> iterator =this.filter.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> entry = iterator.next();
            str.append("filterKey:"+entry.getKey()+"--"+"filterValue:"+entry.getValue()+"\n");
        }
        return str.toString();
    }
}
