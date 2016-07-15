package com.tw.service;

import com.tw.entity.Item;
import com.tw.model.PayItem;
import com.tw.service.promotion.NoPromotion;
import com.tw.service.promotion.Promotion;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class PosService {
    public Map<String, Item> itemMap;

    @Autowired
    public ItemService itemService;

    @Autowired
    public OutputService outputService;

    private Promotion promotion;

    public PosService() {
        promotion = new NoPromotion();
    }

    public PosService(String promotionInfo){
        setPromotionInfo(promotionInfo);
    }

    public void setPromotionInfo(String promotionInfo){
        promotion = new PromotionFactory().createPromotion(promotionInfo);
    }

    public String generateReceiptFromItemList(String items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("input string is empty");
        }
        ArrayList<PayItem> payItems = transfetJsonToArrayList(items);
        if (payItems.isEmpty()) {
            throw new IllegalArgumentException("there is no pay item to show!");
        }
        return outputService.print(payItems, promotion);
    }

    private ArrayList<PayItem> transfetJsonToArrayList(String items) {
        ArrayList<PayItem> payItems = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(items.toString());
        for (Object item : jsonArray) {
            JSONObject jsonObject = (JSONObject) item;
            addItemToPayItems(payItems, jsonObject);
        }
        return payItems;
    }

    private void addItemToPayItems(ArrayList<PayItem> payItems, JSONObject jsonObject) {
        if (objectNotInList(jsonObject, payItems)) {
            insertObjectToPayItems(payItems, jsonObject);
        } else {
            useObjectToUpdatePayItems(jsonObject, payItems);
        }
    }

    private void insertObjectToPayItems(ArrayList<PayItem> payItems, JSONObject jsonObject) {
        PayItem payItem = new PayItem(jsonObject.getString("name"), jsonObject.getString("barcode"),
                jsonObject.getString("unit"), jsonObject.getDouble("price"), jsonObject.getInt("count"));
        payItems.add(payItem);
    }

    private void useObjectToUpdatePayItems(JSONObject jsonObject, ArrayList<PayItem> payItems) {
        PayItem item = new PayItem("", "", "", 0.00, -1);
        int i = -1;
        for (PayItem oldItem : payItems) {
            ++i;
            if (oldItem.getBarcode().equals(jsonObject.getString("barcode"))) {
                if (-1 == item.getCount()) {
                    item = oldItem;
                }
                item.setCount(oldItem.getCount() + jsonObject.getInt("count"));
                payItems.set(i, oldItem);
                break;
            }
        }
    }

    private boolean objectNotInList(JSONObject jsonObject, ArrayList<PayItem> payItems) {
        for (PayItem item : payItems) {
            if (item.getBarcode().equals(jsonObject.getString("barcode"))) {
                return false;
            }
        }
        return true;
    }

    public String generateReceiptFromBarcodes(String inputs) {
        itemMap = itemService.getItemMap();
        JSONArray inputsJson = new JSONArray(inputs);
        if (inputsJson.length() == 0) {
            throw new IllegalArgumentException("there is no barcodes");
        }
        ArrayList<PayItem> payItems = new ArrayList<>();
        for (Object input : inputsJson) {
            if (input.toString().contains("-")) {
                PayItem newItem = getItemWithCountByCompoundBarcode(input);
                payItems.add(newItem);
            } else {
                payItems.add(convertToPayItem(itemMap.get(input)));
            }
        }
        return generateReceiptFromItemList((new JSONArray(payItems)).toString());
    }

    private PayItem getItemWithCountByCompoundBarcode(Object item) {
        String[] barcodeCount = item.toString().split("-");
        Item tempItem = itemMap.get(barcodeCount[0]);
        int count = Integer.valueOf(barcodeCount[1]);
        if (count < 0) {
            throw new IllegalArgumentException(barcodeCount[0] + "'s count is "
                    + barcodeCount[1] + ", can't be negative");
        }
        //need to allocate new memory to avoid all item point to the same memory in payItemHashMap
        PayItem newItem = new PayItem(tempItem.getName(), tempItem.getBarcode(),
                tempItem.getUnit(), tempItem.getPrice(), count);
        newItem.setCount(count);
        return newItem;
    }

    private PayItem convertToPayItem(Item item) {
        return new PayItem(item.getName(), item.getBarcode(), item.getUnit(), item.getPrice(), 1);
    }

    public void setOutputService(OutputService outputService) {
        this.outputService = outputService;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }
}
