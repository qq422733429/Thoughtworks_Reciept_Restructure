package com.tw.service;

import com.tw.service.promotion.NoPromotion;
import com.tw.service.promotion.Promotion;
import com.tw.service.promotion.PromotionBuyThreeGetOneFree;
import com.tw.service.promotion.PromotionByType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PromotionFactory {

    public Promotion createPromotion(String input){
        List<JSONObject> objList = new ArrayList<JSONObject>();
        try {
            JSONObject jsonObj = new JSONObject(input);
            objList.add(jsonObj);
        } catch(JSONException e){
            JSONArray jsonArray = new JSONArray(input);
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                objList.add(jsonObj);
            }
        }

        PromotionType type;
        String barcodes;
        for (int i = 0; i < objList.size(); ++i) {
            JSONObject jsonObj = objList.get(i);
            try{
                type = PromotionType.valueOf(jsonObj.getString("type"));
                barcodes = jsonObj.getString("barcodes");
                return createPromotionByInput(type, barcodes);
            } catch (Exception e){
                System.out.println("catch exception:" + e.getMessage());
            }
        }
        return new NoPromotion();
    }

    private Promotion createPromotionByInput(PromotionType type, String barcodes) {
        switch(type){
            case SELL_BY_TYPE: return new PromotionByType(barcodes);
            case BUY_THREE_GET_ONE_FREE: return new PromotionBuyThreeGetOneFree(barcodes);
            default:return new NoPromotion();
        }
    }

}
