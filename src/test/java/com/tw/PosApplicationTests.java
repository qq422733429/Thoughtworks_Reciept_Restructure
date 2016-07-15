package com.tw;

import com.tw.entity.Item;
import com.tw.service.ItemService;
import com.tw.service.OutputService;
import com.tw.service.PosService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PosApplicationTests {

    private PosService posService;

    @Mock
    ItemService itemService;

    private Map<String, Item> itemMap;
    private String expectResult = "";

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        posService = new PosService();
        posService.itemService = itemService;
        posService.setOutputService(new OutputService());


        expectResult = "\\*\\*\\*<没钱赚商店>购物清单\\*\\*\\*\n"
                + "名称：可口可乐，数量：5瓶，单价：3.00\\(元\\)，小计：15.00\\(元\\)\n"
                + "名称：雪碧，数量：2瓶，单价：3.00\\(元\\)，小计：6.00\\(元\\)\n"
                + "名称：电池，数量：1个，单价：2.00\\(元\\)，小计：2.00\\(元\\)\n"
                + "-{20,50}\n"
                + "总计：23.00\\(元\\)\n"
                + "\\*{20,50}";
        itemMap = new HashMap<>();
        itemMap.put("ITEM000000", new Item("ITEM000000", "可口可乐", "瓶", "", "", 3.00));
        itemMap.put("ITEM000001", new Item("ITEM000001", "雪碧", "瓶", "", "", 3.00));
        itemMap.put("ITEM000004", new Item("ITEM000004", "电池", "个", "", "", 2.00));
        posService.itemMap = itemMap;
    }

    @Test
    public void testGenerateReceiptFromItemList() {
        String items = "[ { barcode: 'ITEM000000', name: '可口可乐', unit: '瓶', price: 3.00, count: 5 }, "
                + " { barcode: 'ITEM000001', name: '雪碧', unit: '瓶', price: 3.00, count: 2 }, "
                + " { barcode: 'ITEM000004', name: '电池', unit: '个', price: 2.00, count: 1 } ] ";

        String result = posService.generateReceiptFromItemList(items);

        assertTrue(result.matches(expectResult));
    }

    @Test
    public void testGenerateReceiptFromItemListWithRepeat() {
        String items = "[ { barcode: 'ITEM000000', name: '可口可乐', unit: '瓶', price: 3.00, count: 2 }, "
                + " { barcode: 'ITEM000000', name: '可口可乐', unit: '瓶', price: 3.00, count: 2 }, "
                + " { barcode: 'ITEM000000', name: '可口可乐', unit: '瓶', price: 3.00, count: 1 }, "
                + " { barcode: 'ITEM000001', name: '雪碧', unit: '瓶', price: 3.00, count: 1 }, "
                + " { barcode: 'ITEM000001', name: '雪碧', unit: '瓶', price: 3.00, count: 1 }, "
                + " { barcode: 'ITEM000004', name: '电池', unit: '个', price: 2.00, count: 1 } ] ";
        when(itemService.getItemMap()).thenReturn(itemMap);

        String result = posService.generateReceiptFromItemList(items);

        assertTrue(result.matches(expectResult));
    }

    @Test
    public void testGenerateReceiptFromBarcodes() {
        String barcodes = "['ITEM000000','ITEM000000','ITEM000000','ITEM000000',"
                + "'ITEM000000','ITEM000001','ITEM000001', 'ITEM000004' ]";

        when(itemService.getItemMap()).thenReturn(itemMap);

        String result = posService.generateReceiptFromBarcodes(barcodes);
        assertTrue(result.matches(expectResult));
    }

    @Test
    public void testGenerateReceiptFromBarcodesWithRepeat() {
        String barcodes = "['ITEM000000-3','ITEM000000-2','ITEM000001-2', 'ITEM000004']";
        when(itemService.getItemMap()).thenReturn(itemMap);

        String result = posService.generateReceiptFromBarcodes(barcodes);

        assertTrue(result.matches(expectResult));
    }

    @Test
    public void testGenerateReceiptPromotionByType() {
        String barcodes = "['ITEM000000-10','ITEM000000-2','ITEM000001-2', 'ITEM000004']";
        posService.setPromotionInfo("[{type: 'SELL_BY_TYPE',barcodes: 'ITEM000000'}]");

        expectResult = "\\*\\*\\*<没钱赚商店>购物清单\\*\\*\\*\n"
                + "名称：可口可乐，数量：12瓶，单价：3.00\\(元\\)，小计：34.20\\(元\\),优惠：1.80\\(元\\)\n"
                + "名称：雪碧，数量：2瓶，单价：3.00\\(元\\)，小计：6.00\\(元\\)\n"
                + "名称：电池，数量：1个，单价：2.00\\(元\\)，小计：2.00\\(元\\)\n"
                + "-{20,50}\n"
                + "批发价出售商品\n"
                + "名称：可口可乐，数量：12瓶\n"
                + "-{20,50}\n"
                + "总计：42.20\\(元\\)\n"
                + "节省：1.80\\(元\\)\n"
                + "\\*{20,50}";
        when(itemService.getItemMap()).thenReturn(itemMap);

        String result = posService.generateReceiptFromBarcodes(barcodes);

        assertTrue(result.matches(expectResult));
    }

    @Test
    public void testGenerateReceiptPromotionByBuyThreeFreeOne() {
        String barcodes = "['ITEM000000-10','ITEM000000-2','ITEM000001-2', 'ITEM000004']";
        posService.setPromotionInfo("[{type: 'BUY_THREE_GET_ONE_FREE',barcodes: 'ITEM000000'}]");

        expectResult = "\\*\\*\\*<没钱赚商店>购物清单\\*\\*\\*\n"
                + "名称：可口可乐，数量：12瓶，单价：3.00\\(元\\)，小计：36.00\\(元\\)\n"
                + "名称：雪碧，数量：2瓶，单价：3.00\\(元\\)，小计：6.00\\(元\\)\n"
                + "名称：电池，数量：1个，单价：2.00\\(元\\)，小计：2.00\\(元\\)\n"
                + "-{20,50}\n"
                + "买三免一商品：\n"
                + "名称：可口可乐，数量：4瓶\n"
                + "-{20,50}\n"
                + "总计：32.00\\(元\\)\n"
                + "节省：12.00\\(元\\)\n"
                + "\\*{20,50}";
        when(itemService.getItemMap()).thenReturn(itemMap);

        String result = posService.generateReceiptFromBarcodes(barcodes);
        System.out.println(result);
        assertTrue(result.matches(expectResult));
    }
}
