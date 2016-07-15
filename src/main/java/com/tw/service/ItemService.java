package com.tw.service;

import com.tw.dao.ItemRepository;
import com.tw.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService {

    @Autowired
    public ItemRepository itemRepository;

    public Map<String, Item> getItemMap() {
        HashMap<String, Item> itemMap = new HashMap<>();
        List<Item> itemList = itemRepository.findAll();

        for (Item item : itemList) {
            itemMap.put(item.getBarcode(), item);
        }
        return itemMap;
    }
}
