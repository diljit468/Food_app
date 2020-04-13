package com.example.foodtask;

import org.json.JSONArray;

import java.util.List;

public class MainModel {
    String name;
    JSONArray array;

    public MainModel(String name, JSONArray array) {
        this.name = name;
        this.array = array;
    }

    public String getName() {
        return name;
    }

    public JSONArray getArray() {
        return array;
    }
}
