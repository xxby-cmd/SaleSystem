import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderDraft {
    private final List<OrderItem> orders=new ArrayList<OrderItem>();
    //有参构造方法
    public OrderDraft(Quotation quotation){
        if (quotation == null) {
            throw new NullPointerException("订单为空");
        }else if(quotation.getStatus() != QuotationStatus.CONFIRMED){
            throw new IllegalStateException("订单状态必须为确认状态");
        }
        for (int i = 0; i < quotation.getItems().size(); i++) {
            orders.add(new OrderItem(quotation.getItems().get(i)));
        }
    }
    //从报价单转换为订单草稿
    public static OrderDraft from(Quotation q){
        return new OrderDraft(q);
    }
    //获取订单明细
    public List<OrderItem> getOrders() {
        return List.copyOf(orders);
    }
    //计算订单总金额
    public BigDecimal getTotal() {
        BigDecimal price=BigDecimal.ZERO;
        for (OrderItem orderItem : orders) {
            price=price.add(orderItem.getSum());
        }
        return price;
    }
}
