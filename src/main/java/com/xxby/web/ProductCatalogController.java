package com.xxby.web;

import com.xxby.Product.ProductCatalogService;
import com.xxby.Product.ProductModel;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/productCatalog")

public class ProductCatalogController {
        private final ProductCatalogService service;
        public ProductCatalogController(ProductCatalogService service) {
                this.service = service;
        }

        @PostMapping("/addProductCatalog")
        public void addProductCatalog(@RequestBody ProductCatalogCreateRequest request) {
                service.addProductCatalog(request);
        }

        @GetMapping("/findByProductCode")
        public ProductModel findByProductCode(@RequestParam String productCode) throws SQLException {
                return service.findByProductCode(productCode);
        }
}
