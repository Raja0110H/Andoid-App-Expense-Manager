package com.expense.manager.Model.bean;

import java.util.List;

public class ExpandebleListData {
    CategoryBean a;
    List<DataBean> b;

    public CategoryBean getCategory() {
        return this.a;
    }

    public void setCategory(CategoryBean categoryBean) {
        this.a = categoryBean;
    }

    public List<DataBean> getCategoryData() {
        return this.b;
    }

    public void setCategoryData(List<DataBean> list) {
        this.b = list;
    }
}
