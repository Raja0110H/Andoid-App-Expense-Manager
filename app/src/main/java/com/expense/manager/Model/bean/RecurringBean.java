package com.expense.manager.Model.bean;

public class RecurringBean {
    private String accountRef = "";
    private String amount = "";
    private String categoryId = "";
    private String categoryLimit = "";
    private CategoryBean categoryModel;
    private String f94id = "";
    private String image = "";
    private String method = "";
    private String note = "";
    private String recurringDate = "";
    private String recurringLastdate = "";
    private String recurringType = "";
    private String subCategoryId = "";

    public RecurringBean(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11) {
        this.categoryId = str;
        this.subCategoryId = str2;
        this.amount = str3;
        this.note = str5;
        this.recurringType = str6;
        this.recurringDate = str7;
        this.recurringLastdate = str8;
        this.method = str9;
        this.image = str10;
        this.categoryLimit = str11;
    }

    public RecurringBean() {
    }

    public String getAccountRef() {
        return this.accountRef;
    }

    public void setAccountRef(String str) {
        this.accountRef = str;
    }

    public CategoryBean getCategoryModel() {
        return this.categoryModel;
    }

    public void setCategoryModel(CategoryBean categoryBean) {
        this.categoryModel = categoryBean;
    }

    public String getId() {
        return this.f94id;
    }

    public void setId(String str) {
        this.f94id = str;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String str) {
        this.categoryId = str;
    }

    public String getSubCategoryId() {
        return this.subCategoryId;
    }

    public void setSubCategoryId(String str) {
        this.subCategoryId = str;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String str) {
        this.amount = str;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String str) {
        this.note = str;
    }

    public String getRecurringType() {
        return this.recurringType;
    }

    public void setRecurringType(String str) {
        this.recurringType = str;
    }

    public String getRecurringDate() {
        return this.recurringDate;
    }

    public void setRecurringDate(String str) {
        this.recurringDate = str;
    }

    public String getRecurringLastdate() {
        return this.recurringLastdate;
    }

    public void setRecurringLastdate(String str) {
        this.recurringLastdate = str;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String str) {
        this.method = str;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String str) {
        this.image = str;
    }

    public String getCategoryLimit() {
        return this.categoryLimit;
    }

    public void setCategoryLimit(String str) {
        this.categoryLimit = str;
    }

    public String toString() {
        return "Amount " + this.amount + " Note " + this.note;
    }

    public DataBean convertToDataBean() {
        DataBean dataBean = new DataBean();
        dataBean.setAmount(this.amount);
        dataBean.setCategoryId(this.categoryId);
        dataBean.setSubCategoryId(this.subCategoryId);
        dataBean.setNote(this.note);
        dataBean.setMethod(this.method);
        dataBean.setImage(this.image);
        dataBean.setCategoryLimit(this.categoryLimit);
        dataBean.setDate(this.recurringLastdate);
        dataBean.setRecurringModel(this);
        dataBean.setRefRecurring(this.f94id);
        dataBean.setCategoryModel(this.categoryModel);
        dataBean.setAccountRef(this.accountRef);
        return dataBean;
    }
}
