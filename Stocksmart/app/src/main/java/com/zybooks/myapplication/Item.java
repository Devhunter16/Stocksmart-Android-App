package com.zybooks.myapplication;

public class Item {

    int id;
    String desc;
    String qty;
    String unit;

    public Item() {
        super();
    }

    public Item(int i, String description, String quantity, String unit) {
        super();
        this.id = i;
        this.desc = description;
        this.qty = quantity;
        this.unit = unit;
    };

    // Constructor
    public Item(String description, String quantity, String unit) {
        this.desc = description;
        this.qty = quantity;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
};
