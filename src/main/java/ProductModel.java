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
        //判断产品名称是否合法
        ProductValidUtil.validProductCode(productCode);
        //标准化产品名称
        productCode = productCode.trim().toUpperCase(Locale.ROOT);
        //判断产品名称是否重复
        ProductValidUtil.validProductCode(productCode,productList);
        //获取索引并更新产品名称
        if(this.productCode==null){
            this.productCode = productCode;
            productList.add(this);
        }else {
            productList.get(productList.indexOf(this)).productCode = productCode;
        }
    }

}
