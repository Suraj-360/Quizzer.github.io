package com.suraj.quizzer;

public class DomainModel {

    String categoryId;
    String categoryName;
    String imgId;

    public DomainModel() {

    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId, String imgId) {
        this.categoryId = categoryId;
        this.imgId = imgId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public DomainModel(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
