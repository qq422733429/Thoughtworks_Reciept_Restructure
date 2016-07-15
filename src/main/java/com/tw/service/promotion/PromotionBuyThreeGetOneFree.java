package com.tw.service.promotion;

import com.tw.model.PayItem;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class PromotionBuyThreeGetOneFree implements Promotion {
    public static final int MIN_NUM = 10;
    public static final double DISCOUNT = 0.95;
    public static final double PRECISION = 0.00001;
    private int minNum = MIN_NUM;
    private double discount = DISCOUNT;
    List<String> barcodes = new ArrayList<String>();

    public PromotionBuyThreeGetOneFree(String barcodes) {
        loadPromotionItems(barcodes);
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

    @Override
    public boolean isValidPromotionItem(PayItem item){
        for(String barcode:barcodes){
            if (barcode.equals(item.getBarcode())){
                return true;
            }
        }
        return false;
    }

    @Override
    public double getItemSave(PayItem item) {
        return item.getCount()/3*item.getPrice();
    }

    @Override
    public String getItemLine(PayItem item){
        return item.toString() + "，小计：" + String.format("%.2f", item.getPrice() * item.getCount()) + "(元)\n";

    }

    @Override
    public String getPromotionName(){
        return "买三免一商品：\n";
    }

    @Override
    public String getItemPromotionInfo(PayItem item){
        return "名称：" + item.getName() +"，数量：" + item.getCount()/3 + item.getUnit() + "\n";
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
