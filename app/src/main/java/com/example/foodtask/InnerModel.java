package com.example.foodtask;

public class InnerModel {
    String name,url,des;
    int price;

    public InnerModel(String name, String url, String des, int price) {
        this.name = name;
        this.url = url;
        this.des = des;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDes() {
        return des;
    }

    public int getPrice() {
        return price;
    }
}
