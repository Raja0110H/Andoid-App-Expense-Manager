package com.expense.manager.Model.bean;

import java.util.List;

public class CategoryBean {
    private String accountRef = "";
    private int categoryGroup = 0;
    private String categoryLimit = "";
    private String categoryTotal = "";
    private String color = "";
    private int drag_id = 0;
    private String f92id = "";
    private List<DataBean> managerData;
    private String name = "";
    private List<RecurringBean> recurringData;

    public CategoryBean(String str, String str2, int i, int i2, String str3) {
        this.name = str;
        this.color = str2;
        this.categoryGroup = i;
        this.drag_id = i2;
        this.categoryLimit = str3;
    }

    public CategoryBean(String str, String str2, int i, int i2) {
        this.name = str;
        this.color = str2;
        this.categoryGroup = i;
        this.drag_id = i2;
        this.categoryLimit = "0";
    }

    public CategoryBean() {
    }

    public String getId() {
        return this.f92id;
    }

    public void setId(String str) {
        this.f92id = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String str) {
        this.color = str;
    }

    public int getCategoryGroup() {
        return this.categoryGroup;
    }

    public void setCategoryGroup(int i) {
        this.categoryGroup = i;
    }

    public int getDrag_id() {
        return this.drag_id;
    }

    public void setDrag_id(int i) {
        this.drag_id = i;
    }

    public String getCategoryTotal() {
        return this.categoryTotal;
    }

    public void setCategoryTotal(String str) {
        this.categoryTotal = str;
    }

    public String toString() {
        return "Name " + this.name + " Color " + this.color;
    }

    public String getAccountRef() {
        return this.accountRef;
    }

    public void setAccountRef(String str) {
        this.accountRef = str;
    }

    public String getCategoryLimit() {
        return this.categoryLimit;
    }

    public void setCategoryLimit(String str) {
        this.categoryLimit = str;
    }

    public List<DataBean> getManagerData() {
        return this.managerData;
    }

    public void setManagerData(List<DataBean> list) {
        this.managerData = list;
    }

    public List<RecurringBean> getRecurringData() {
        return this.recurringData;
    }

    public void setRecurringData(List<RecurringBean> list) {
        this.recurringData = list;
    }
}
