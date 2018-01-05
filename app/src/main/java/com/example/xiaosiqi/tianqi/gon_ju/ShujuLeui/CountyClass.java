package com.example.xiaosiqi.tianqi.gon_ju.ShujuLeui;

/**
 * Created by xiaosiqi on 2017/12/19.
 */

public class CountyClass {
    int id;
    String name;
    String code;

    public CountyClass(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
