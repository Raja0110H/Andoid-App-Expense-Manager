package com.expense.manager.Model.bean;

public class DataBean {
    private String accountRef = "";
    private String amount = "";
    private String categoryId = "";
    private String categoryLimit = "";
    private CategoryBean categoryModel;
    private String date = "";
    private String f93id = "";
    private String image = "";
    private String method = "";
    private String note = "";
    private RecurringBean recurringModel;
    private String refRecurring = "";
    private String subCategoryId = "";

    public DataBean(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
        this.categoryId = str;
        this.subCategoryId = str2;
        this.amount = str3;
        this.date = str4;
        this.note = str5;
        this.method = str6;
        this.image = str7;
        this.categoryLimit = str8;
    }

    public DataBean() {
    }

    public String getAccountRef() {
        return this.accountRef;
    }

    public void setAccountRef(String str) {
        this.accountRef = str;
    }

    public String getId() {
        return this.f93id;
    }

    public void setId(String str) {
        this.f93id = str;
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

    public String getDate() {
        return this.date;
    }

    public void setDate(String str) {
        this.date = str;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String str) {
        this.note = str;
    }

    public String getRefRecurring() {
        return this.refRecurring;
    }

    public void setRefRecurring(String str) {
        this.refRecurring = str;
    }

    public CategoryBean getCategoryModel() {
        return this.categoryModel;
    }

    public void setCategoryModel(CategoryBean categoryBean) {
        this.categoryModel = categoryBean;
    }

    public RecurringBean getRecurringModel() {
        return this.recurringModel;
    }

    public void setRecurringModel(RecurringBean recurringBean) {
        this.recurringModel = recurringBean;
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

    public RecurringBean convertToRecurringBean() {
        RecurringBean recurringBean = new RecurringBean();
        recurringBean.setAmount(this.amount);
        recurringBean.setCategoryId(this.categoryId);
        recurringBean.setSubCategoryId(this.subCategoryId);
        recurringBean.setNote(this.note);
        recurringBean.setCategoryLimit(this.categoryLimit);
        recurringBean.setRecurringLastdate(this.date);
        recurringBean.setAccountRef(this.accountRef);
        recurringBean.setImage(this.image);
        recurringBean.setMethod(this.method);
        return recurringBean;
    }
}
