import java.math.BigDecimal;

public class OrderItem {
    private final String productCode;
    private final int quantity;
    private final BigDecimal price;
    //有参构造方法
    public OrderItem(QuotationItem qI) {
        if (qI == null) {
            throw new IllegalArgumentException("订单为空");
        }
        else if (qI.getQuantity() == 0||qI.getProductCode().isEmpty()||qI.getPrice() == null) {
            throw new IllegalArgumentException("订单参数不完整，请补全商品编码、数量、价格");
        }
        this.productCode = qI.getProductCode();
        this.quantity = qI.getQuantity();
        this.price = qI.getPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getProductCode() {
        return productCode;
    }

    public BigDecimal getSum() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

}
