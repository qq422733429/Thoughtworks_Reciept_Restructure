package com.tw.service.promotion;

import com.tw.model.PayItem;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class PromotionByType implements Promotion{
    public static final int MIN_NUM = 10;
    public static final double DISCOUNT = 0.95;
    public static final double PRECISION = 0.00001;

    private int minNum = MIN_NUM;
    private double discount = DISCOUNT;
    List<String> barcodes = new ArrayList<String>();

    public PromotionByType(String promotionInfo) {
        loadPromotionItems(promotionInfo);
    }

    public PromotionByType(double discount, int minNum, String promotionInfo) {
        this.discount = discount;
        this.minNum = minNum;
        loadPromotionItems(promotionInfo);
    }

    private void loadPromotionItems(String promotinBarcodes){
        if (promotinBarcodes.contains("[")) {
            JSONArray codesArray = new JSONArray(promotinBarcodes);
            for(int j = 0; j < codesArray.length(); ++j){
                barcodes.add(codesArray.getString(j));
            }
        } else {
            barcodes.add(promotinBarcodes);
        }
    }

    public boolean isValidPromotionItem(PayItem item){
        for(String barcode:barcodes){
            if (barcode.equals(item.getBarcode())){
                return true;
            }
        }
        return false;
    }

    public double getItemSave(PayItem item) {
        if (item.getCount() > minNum){
            return item.getCount() * item.getPrice() * (1-this.discount);
        } else {
            return 0;
        }
    }

    public String getItemLine(PayItem item){
        double savedMoney = getItemSave(item);
        if (Math.abs(savedMoney) > PRECISION){
            return item.toString() + "，小计：" + String.format("%.2f", item.getPrice() * item.getCount() - savedMoney) + "(元)"
                + ",优惠：" + String.format("%.2f", savedMoney)  + "(元)\n";
        } else {
            return item.toString() + "，小计：" + String.format("%.2f", item.getPrice() * item.getCount() - savedMoney) + "(元)\n";
        }
    }

    public String getPromotionName(){
        return "批发价出售商品\n";
    }
    public String getItemPromotionInfo(PayItem item){
        return "名称：" + item.getName() +"，数量：" + item.getCount() + item.getUnit() + "\n";
    }

    public int getMinNum() {
        return minNum;
    }

    public void setMinNum(int minNum) {
        this.minNum = minNum;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
