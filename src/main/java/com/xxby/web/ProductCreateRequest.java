package com.xxby.web;

import com.xxby.Product.ProductInfo;

public record ProductCreateRequest(
        String productCode,
        ProductInfo.ProductName productName,
        ProductInfo.Brand brand,
        ProductInfo.Unit unit) {
}
