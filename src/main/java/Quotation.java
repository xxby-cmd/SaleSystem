import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Quotation {
    private List<QuotationItem> items = new ArrayList<>();

    //有参构造
    public Quotation(QuotationItem item){
        //添加明细
        ValidationUtil.validQuotationItem(item);
        items.add(item);
    }

    //无参构造
    public Quotation(){}

    //添加明细
    public void addItem(QuotationItem item) {
        //添加明细
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
        //判断产品名称是否合法
        ValidationUtil.validProductCode(ProductCode);
        //标准化产品名称
        String normalizedProductCode = ProductCode.trim().toUpperCase(Locale.ROOT);

        return items.removeIf(i->i.getProductCode().equals(normalizedProductCode));
    }

}
