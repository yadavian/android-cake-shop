package com.vesit.cakeshopandroid.model;

public class CategoryModel {
    String category_name;

    public CategoryModel() {
    }

    public CategoryModel(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
