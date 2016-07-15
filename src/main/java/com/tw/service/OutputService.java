package com.tw.service;

import com.tw.model.PayItem;
import com.tw.service.promotion.Promotion;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class OutputService {

    public static final double PRECISION = 0.00001;

    private String storeName = "没钱赚商店";

    public OutputService() {
    }

    public OutputService(String storeName) {
        this.storeName = storeName;
    }

    public String print(ArrayList<PayItem> payItems, Promotion promotion) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("***<" + storeName + ">购物清单***\n");

        double sum = 0.00;
        double saveMoney = 0.00;
        double savePerItem = 0.00;
        String promotionDetail = "";
        for (PayItem payItem : payItems) {
            sum += payItem.getPrice() * payItem.getCount();
            if (promotion.isValidPromotionItem(payItem)){
                savePerItem = promotion.getItemSave(payItem);
                if (Math.abs(savePerItem) > PRECISION) {
                    saveMoney += savePerItem;
                    promotionDetail += promotion.getItemPromotionInfo(payItem);
                }
            }
            stringBuilder.append(promotion.getItemLine(payItem));
        }

        if (!promotionDetail.isEmpty()) {
            stringBuilder.append("-----------------------------\n");
            stringBuilder.append(promotion.getPromotionName());
            stringBuilder.append(promotionDetail);
        }

        stringBuilder.append("-----------------------------\n");
        stringBuilder.append("总计：" + String.format("%.2f", sum - saveMoney) + "(元)\n");
        if (!promotionDetail.isEmpty()){
            stringBuilder.append("节省：" + String.format("%.2f", saveMoney) + "(元)\n");
        }
        stringBuilder.append("******************************");
        return stringBuilder.toString();
    }
}