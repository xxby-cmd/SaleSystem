package com.xxby.web;

import com.xxby.Product.ProductModel;

import java.util.List;

public record ProductCatalogCreateRequest(
        String productCatalogName,
        List<ProductCreateRequest> productCatalog) {
}
