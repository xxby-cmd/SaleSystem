package com.xxby.校验规则;

import com.xxby.Quotation.QuotationItem;
import com.xxby.web.ProductNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ValidationUtil {
    //工具类不能创建对象
    private ValidationUtil() {}
    //过滤产品编码
    public static void validProductCode(String productCode) {
        if(productCode ==null|| productCode.isEmpty()){
            throw new NullPointerException("产品编码不能为空");
        }else if(productCode.isBlank()){
            throw new IllegalArgumentException("产品编码不能为纯空格");
        }
    }
    //判断产品编码是否已存在
    /*
    public static void validProductCode(String ProductCode) {
        for(com.xxby.Product.ProductModel item:productList){
            if(item.getProductCode().equals(ProductCode)){
                throw new IllegalArgumentException("产品编码已存在");
            }
        }
    }
    */

    //判断数量合法
    public static void validQuantity(int quantity) {
        if(quantity<=0){
            throw new IllegalArgumentException("数量必须大于0");
        }

    }
    //判断价格合法
    public static void validPrice(BigDecimal price) {
        if (price == null) {
            throw new NullPointerException("价格不能为空");
        } else if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("价格必须不能小于0");
        }
    }
    //判断报价单明细是否合法
    public static void validQuotationItem(QuotationItem item) {
        if (item == null) {
            throw new NullPointerException("报价单明细不存在");
        } else if (item.getProductCode() == null || item.getQuantity() == 0 || item.getPrice() == null) {
            throw new IllegalArgumentException("报价单明细不完整");
        }
    }
    //判断有效期是否合法
    public static void validValidUntil(LocalDate validUntil) {
        if (validUntil == null) {
            throw new NullPointerException("有效期不能为空");
        } else if (validUntil.isBefore(LocalDate.of(2026,8,23))) {
            throw new IllegalArgumentException("有效期不能早于2026-08-23");
        }
    }

}
