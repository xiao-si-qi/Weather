package com.example.xiaosiqi.tianqi.gon_ju.ShujuLeui;

/**
 * Created by xiaosiqi on 2017/12/19.
 */

public class ProvinceClass {
    int id;
    String name;
    String zone;

    public ProvinceClass(int id, String name, String zone) {
        this.id = id;
        this.name = name;
        this.zone = zone;
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

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
