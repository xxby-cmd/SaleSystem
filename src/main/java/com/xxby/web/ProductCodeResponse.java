package com.xxby.web;

import com.xxby.Product.ProductInfo;

public record ProductCodeResponse(
        String productCode,
        ProductInfo.Series series,
        int boreDiameter,//缸径
        int strokeLength,//行程
        ProductInfo.Suffix suffix) {

}


