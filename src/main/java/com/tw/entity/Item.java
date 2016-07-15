package com.tw.entity;

import javax.persistence.*;

@Entity
@Table(name = "ITEM")
public class Item {

    @Id
    @Column(name = "BARCODE")
    private String barcode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "SUB_CATEGORY")
    private String subCategory;

    @Column(name = "PRICE")
    private double price;

    public Item() {
    }

    public Item(String barcode, String name, String unit, String category, String subCategory, double price) {
        this.name = name;
        this.barcode = barcode;
        this.unit = unit;
        this.price = price;
        this.subCategory = subCategory;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
