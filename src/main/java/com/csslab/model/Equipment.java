package com.csslab.model;

public class Equipment {
    private final String code;
    private final String name;
    private final String category;
    private String status;

    public Equipment(String code, String name, String category, String status) {
        this.code = code;
        this.name = name;
        this.category = category;
        this.status = status;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "[" + code + "] " + name;
    }
}