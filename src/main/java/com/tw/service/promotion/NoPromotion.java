package com.tw.service.promotion;

import com.tw.model.PayItem;

public class NoPromotion implements Promotion {

    public boolean isValidPromotionItem(PayItem item){
        return false;
    }
    public String getItemLine(PayItem item) {
        return item.toString() + "，小计：" + String.format("%.2f", item.getPrice() * item.getCount()) + "(元)\n";
    }

    @Override
    public double getItemSave(PayItem item) {
        return 0;
    }

    public String getPromotionName(){
        return "";
    }
    public String getItemPromotionInfo(PayItem item){
        return "";
    }

}
