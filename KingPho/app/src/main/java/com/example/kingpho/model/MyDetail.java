package com.example.kingpho.model;

public class MyDetail {
    private String properties;
    private String info;

    public MyDetail(String properties, String info) {
        this.properties = properties;
        this.info = info;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
