import java.math.BigDecimal;
import java.util.Locale;

public class QuotationItem {
    private String ProductCode;
    private int quantity;
    private BigDecimal price;

    public QuotationItem() {}
    public QuotationItem(String ProductCode, int quantity, BigDecimal price) {
        //判断商品编码是否合法
        ValidationUtil.validProductCode(ProductCode);
        //标准化商品编码为大写且去掉首尾空格，赋值
        this.ProductCode = ProductCode.trim().toUpperCase(Locale.ROOT);
        //判断数量合法
        ValidationUtil.validQuantity(quantity);
        //赋值数量
        this.quantity = quantity;
        //判断价格合法
        ValidationUtil.validPrice(price);
        //赋值价格
            this.price=price;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String ProductCode) {
        ValidationUtil.validProductCode(ProductCode);
        this.ProductCode = ProductCode.trim().toUpperCase(Locale.ROOT);;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        ValidationUtil.validQuantity(quantity);
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        //判断价格合法
        ValidationUtil.validPrice(price);
        //判断是否为赠品
        //赋值价格
            this.price=price;
    }

    public BigDecimal getSum() {
        if (ProductCode==null) {
            System.out.println("商品还没有录入编码");
        }else if (quantity==0) {
            System.out.println("商品数量为0");
            return BigDecimal.ZERO;
        } else if(price==null) {
            System.out.println("商品还没有录入价格");
            return BigDecimal.ZERO;
        } else if (price.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        } else{
            return price.multiply(BigDecimal.valueOf(quantity));
        }
        return null;
    }
}
