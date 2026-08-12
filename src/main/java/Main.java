import java.math.BigDecimal;


public class Main {
    public static void main(String[] args) {
        ProductModel productModel = new ProductModel("  Sau32x100");
        ProductModel productModel2 = new ProductModel("    Su32x100");
        System.out.println(productModel.getProductCode());
        QuotationItem quotationItem = new QuotationItem(productModel.getProductCode(),1,new BigDecimal("100.00"));
        System.out.printf("该商品的金额为：%.2f\n",quotationItem.getSum());
        QuotationItem quotationItem2 = new QuotationItem(productModel.getProductCode(),3,new BigDecimal("19.80"));
        System.out.printf("该商品的金额为：%.2f\n",quotationItem2.getSum());
        QuotationItem quotationItem3 = new QuotationItem(productModel2.getProductCode(),3,new BigDecimal("0.10"));
        System.out.printf("该商品的金额为：%.2f\n",quotationItem3.getSum());
        QuotationItem quotationItem4 = new QuotationItem(productModel2.getProductCode(),4,new BigDecimal("0.00"));
        System.out.printf("该商品的金额为：%.2f\n",quotationItem4.getSum());
    }
}
