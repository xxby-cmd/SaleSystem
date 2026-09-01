package com.xxby.web;

import com.xxby.Product.ProductCodeParserService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/product-codes")
public class ProductCodeController {
    private final ProductCodeParserService service;

    public ProductCodeController(ProductCodeParserService service){
        this.service=service;
    }

    @GetMapping("/{productCode}")
    public ProductCodeResponse ProductCodeParser(@PathVariable String productCode){
        return service.parseResponse(productCode);
    }



}
