package com.tw.model;

public class PayItem {
    private String name;
    private String barcode;
    private String unit;
    private double price;
    private int count;

    public PayItem(String name, String barcode, String unit, double price, int count) {
        this.name = name;
        this.barcode = barcode;
        this.unit = unit;
        this.price = price;
        this.count = count;
    }

    @Override
    public String toString() {
        return "名称：" + this.getName()
                + "，数量：" + this.getCount() + this.getUnit()
                + "，单价：" + String.format("%.2f", this.getPrice())
                + "(元)";
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
