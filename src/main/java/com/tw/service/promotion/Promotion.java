package com.tw.service.promotion;

import com.tw.model.PayItem;

public interface Promotion {
    abstract boolean isValidPromotionItem(PayItem item);
    abstract double getItemSave(PayItem item);
    abstract String getItemLine(PayItem item);
    abstract String getPromotionName();
    abstract String getItemPromotionInfo(PayItem item);
}
