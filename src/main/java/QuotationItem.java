import java.math.BigDecimal;
import java.util.Locale;

public class QuotationItem {
    private String ProductCode;
    private int quantity;
    private BigDecimal price;

    public QuotationItem() {}
    public QuotationItem(String ProductCode, int quantity, BigDecimal price) {

        ProductValidUtil.validProductCode(ProductCode);
        this.ProductCode = ProductCode.trim().toUpperCase(Locale.ROOT);
        ProductValidUtil.validQuantity(quantity);
        this.quantity = quantity;
        ProductValidUtil.validPrice(price);
        if (price.compareTo(BigDecimal.ZERO) == 0) {
            this.price = BigDecimal.ZERO;
            System.out.println("该商品："+this.ProductCode+"为赠品");
        } else {
            this.price=price;
        }
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String ProductCode) {
        ProductValidUtil.validProductCode(ProductCode);
        this.ProductCode = ProductCode.trim().toUpperCase(Locale.ROOT);;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        ProductValidUtil.validQuantity(quantity);
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        ProductValidUtil.validPrice(price);
        if (price.compareTo(BigDecimal.ZERO) == 0) {
            this.price = BigDecimal.ZERO;
            System.out.println("该商品："+this.ProductCode+"为赠品");
        } else {
            this.price=price;
        }
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
