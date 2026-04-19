package com.example.backendtemplate.Pojo;

import java.util.List;

public class PageBean {

    private Long total;//总记录数
    private List list;//数据列表

    public PageBean(Long total, List list) {
        this.total = total;
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
