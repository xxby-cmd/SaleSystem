import java.util.ArrayList;
import java.util.Locale;

public class ProductModel {
    private String productCode;
    private ProductInfo.ProductName productName;
    private ProductInfo.Brand brand;
    private ProductInfo.Series series;
    private ProductInfo.Unit unit;
    // 构造方法
    public ProductModel() {}
    public ProductModel(String productCode) {
        //判断产品名称是否合法和重复
        ValidationUtil.validProductCode(productCode);
        productCode = productCode.trim().toUpperCase(Locale.ROOT);
        this.productCode = productCode;
    }
    public ProductModel(String productCode,ProductInfo.ProductName productName,ProductInfo.Brand brand,ProductInfo.Series series,ProductInfo.Unit unit) {
        //判断产品名称是否合法和重复
        ValidationUtil.validProductCode(productCode);
        productCode = productCode.trim().toUpperCase(Locale.ROOT);
        this.productCode = productCode;
        this.productName = productName;
        this.brand = brand;
        this.series = series;
        this.unit = unit;
    }
    //获取产品编码
    public String getProductCode() {
        return productCode;
    }
    //获取产品名称
    public ProductInfo.ProductName getProductName() {
        return productName;
    }
    //设置产品名称
    public void setProductName(ProductInfo.ProductName productName) {
        this.productName = productName;
    }
    //获取品牌
    public ProductInfo.Brand getBrand() {
        return brand;
    }
    //设置品牌
    public void setBrand(ProductInfo.Brand brand) {
        this.brand = brand;
    }
    //获取型号
    public ProductInfo.Series getSeries() {
        return series;
    }
    //设置型号
    public void setSeries(ProductInfo.Series series) {
        this.series = series;
    }
    //获取单位
    public ProductInfo.Unit getUnit() {
        return unit;
    }
    //获取单位中文
    public String getUnitCn() {
        return unit.getUnitCn();
    }
    //设置单位
    public void setUnit(ProductInfo.Unit unit) {
        this.unit = unit;
    }


}
