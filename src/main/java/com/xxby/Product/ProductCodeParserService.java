package com.xxby.Product;

import org.springframework.stereotype.Service;
import com.xxby.web.ProductCodeResponse;

@Service
public class ProductCodeParserService {
    public ProductCodeResponse parseResponse(String productCode) {
        ProductModel temp=new ProductModel(productCode);
            return new ProductCodeResponse(
                    temp.getProductCode(),
                    temp.getSeries(),
                    temp.getBoreDiameter(),
                    temp.getStrokeLength(),
                   temp.getSuffix());
    }

}
