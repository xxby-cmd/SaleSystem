import java.util.ArrayList;
import java.util.Locale;

public class ProductModel {
    private static final ArrayList<ProductModel> productList = new ArrayList<>();


    private String productCode;
    // 构造方法
    public ProductModel() {}
    public ProductModel(String productCode) {
        //判断产品名称是否合法和重复

        ProductValidUtil.validProductCode(productCode);
        productCode = productCode.trim().toUpperCase(Locale.ROOT);
        ProductValidUtil.validProductCode(productCode,productList);
        this.productCode = productCode;
        productList.add(this);
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {

        ProductValidUtil.validProductCode(productCode);

        productCode = productCode.trim().toUpperCase(Locale.ROOT);
        ProductValidUtil.validProductCode(productCode,productList);
        productList.get(productList.indexOf(this)).productCode = productCode;
        this.productCode = productCode;
    }

}
