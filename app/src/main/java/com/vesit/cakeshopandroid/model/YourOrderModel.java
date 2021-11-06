package com.vesit.cakeshopandroid.model;

import java.util.ArrayList;
import java.util.HashMap;

public class YourOrderModel {

    String address;
    String orderDate;
    String orderPrize;
    ArrayList<HashMap<String,Object>> products;

    public YourOrderModel() {
    }

    public YourOrderModel(String address, String orderDate, String orderPrize, ArrayList<HashMap<String, Object>> products) {
        this.address = address;
        this.orderDate = orderDate;
        this.orderPrize = orderPrize;
        this.products = products;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderPrize() {
        return orderPrize;
    }

    public void setOrderPrize(String orderPrize) {
        this.orderPrize = orderPrize;
    }

    public ArrayList<HashMap<String, Object>> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<HashMap<String, Object>> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "YourOrderModel{" +
                "address='" + address + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", orderPrize='" + orderPrize + '\'' +
                ", products=" + products +
                '}';
    }
}
