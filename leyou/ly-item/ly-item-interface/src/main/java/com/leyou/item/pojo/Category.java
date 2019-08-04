package com.leyou.item.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Table(name = "tb_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; //类目名称

    @Column(name = "parent_id")
    private Long parentId; //父类目id,顶级类目填0

    @Column(name = "is_parent")
    private Boolean isParent; //是否为父节点，0为否，1为是

    private Integer sort;  // 排序指数，越小越靠前

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setIsParent(Boolean parent) {
        this.isParent = parent;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getParentId() {
        return parentId;
    }

    @JsonProperty(value = "isParent")
    public Boolean getIsParent() {
        return isParent;
    }

    public Integer getSort() {
        return sort;
    }

}
