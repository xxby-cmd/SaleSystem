package com.xxby.Quotation;

import com.xxby.校验规则.ValidationUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Quotation {
    private List<QuotationItem> items = new ArrayList<>();
    private QuotationStatus status = QuotationStatus.DRAFT;
    private final LocalDate validUntil;

    //复制报价单
    //有参构造
    public Quotation(LocalDate validUntil,QuotationItem item){
        this(validUntil);
        //添加明细,明细必须存在且完整
        ValidationUtil.validQuotationItem(item);
        items.add(item);
    }

    //无参构造
    public Quotation(LocalDate validUntil){
        //判断有效期是否合法
        ValidationUtil.validValidUntil(validUntil);
        this.validUntil = validUntil;
    }

    //添加明细
    public void addItem(QuotationItem item) {
        //判断报价单是否过期
        if(status == QuotationStatus.CONFIRMED){
            throw new IllegalStateException("报价单已确认");
        }
        //添加明细,明细必须存在且完整
        ValidationUtil.validQuotationItem(item);
        //检查是否已存在相同商品的明细
        for(QuotationItem i:items){
            if(i.getProductCode().equals(item.getProductCode())){
                throw new IllegalArgumentException("该型号报价单明细已存在");
            }
        }
        items.add(item);
    }

    //获取报价单数量
    public int getQuantity() {
        return items.size();
    }

    //获取有效期
    public LocalDate getValidUntil() {
        return validUntil;
    }

    //获取报价单状态
    public QuotationStatus getStatus() {
        return status;
    }

    //获取总金额
    public BigDecimal getTotalPrice() {

        BigDecimal totalPrice = BigDecimal.ZERO;
        for(QuotationItem i:items){
            totalPrice = totalPrice.add(i.getSum());
        }
        return totalPrice;
    }

    //获取报价单明细
    public List<QuotationItem> getItems() {
        return List.copyOf(items);
    }

    //根据产品编码删除明细
    public boolean removeItemByProductCode(String ProductCode){
        //判断报价单是否过期
        if(status == QuotationStatus.CONFIRMED){
            throw new IllegalStateException("报价单已确认");
        }
        //判断产品名称是否合法
        ValidationUtil.validProductCode(ProductCode);
        //标准化产品名称
        String normalizedProductCode = ProductCode.trim().toUpperCase(Locale.ROOT);

        return items.removeIf(i->i.getProductCode().equals(normalizedProductCode));
    }

    //过期校验
    public boolean isValidOn(LocalDate today){
        if(today==null){
            throw new IllegalArgumentException("日期不能为空");
        }
        return today.isBefore(validUntil) || today.equals(validUntil);
    }

    //确认报价单
    public void confirmStatus(LocalDate today){
        if(status!=QuotationStatus.DRAFT){
            throw new IllegalArgumentException("报价单状态只能从草稿状态确认为已确认状态");
        } else if(items.isEmpty()){
            throw new IllegalArgumentException("报价单明细不能为空");
        } else if(!this.isValidOn(today)){
            throw new IllegalArgumentException("报价单已过期");
        }
        else{
            this.status = QuotationStatus.CONFIRMED;
        }
    }
}
