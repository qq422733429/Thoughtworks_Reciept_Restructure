package com.tw.controller;

import com.tw.service.PosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
public class PosController {

    @Autowired
    public PosService posService;


    @RequestMapping(value = "/getOutput", method = RequestMethod.POST)
    @ResponseBody
    public String generateReceiptFromBarcodes(@RequestBody String inputs) {
        String receipt = posService.generateReceiptFromBarcodes(inputs);
        return receipt;
    }
}
