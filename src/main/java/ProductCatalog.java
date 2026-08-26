import java.util.HashMap;
import java.util.Map;

public class ProductCatalog {
    private Map<String,ProductModel> productCatalog = new HashMap<>();
    //构造方法
    public ProductCatalog() {}
    public ProductCatalog(ProductModel productModel) {
        if (productModel == null) {
            throw new NullPointerException("产品为空");
        }else if(productModel.getProductCode() == null){
            throw new IllegalArgumentException("产品编码不能为空");
        }else if(productModel.getProductName() == null||productModel.getUnit() == null||productModel.getBrand() == null||productModel.getSeries()== null){
            throw new IllegalArgumentException("产品信息不完整");
        }
        this.productCatalog.put(productModel.getProductCode(),productModel);
    }
    //添加产品
    public void addProductModel(ProductModel productModel) {
        if (productModel == null) {
            throw new NullPointerException("产品为空");
        }else if(productModel.getProductCode() == null) {
            throw new IllegalArgumentException("产品编码不能为空");
        }else if(productModel.getProductName() == null||productModel.getUnit() == null||productModel.getBrand() == null||productModel.getSeries()== null){
                throw new IllegalArgumentException("产品信息不完整");
        }else if(productCatalog.containsKey(productModel.getProductCode())){
            throw new IllegalArgumentException("产品编码已存在");
        }
        productCatalog.put(productModel.getProductCode(),productModel);
    }

    //查询产品
    public ProductModel findByProductCode(String productCode) {
        if (productCode == null) {
            throw new IllegalArgumentException("产品编码不能为空");
        }
        productCode = productCode.trim();
        if(productCode.isEmpty()){
            throw new IllegalArgumentException("产品编码不能为空字符串或全为空格");
        }
        productCode = productCode.toUpperCase();
        return productCatalog.get(productCode);
    }
    //查询是否包含商品
    public boolean containsCode(String productCode) {
        if (productCode == null) {
            throw new IllegalArgumentException("产品编码不能为空");
        }
        productCode = productCode.trim();
        if(productCode.isEmpty()){
            throw new IllegalArgumentException("产品编码不能为空字符串或全为空格");
        }
        productCode = productCode.toUpperCase();

        return productCatalog.containsKey(productCode);
    }
    //查询数量
    public int size() {
        return productCatalog.size();
    }

    /*
    //修改产品信息
    public void modifyProductModel(String productCode, ProductModel updatedProductModel) {
        if (productCode == null) {
            throw new IllegalArgumentException("产品编码不能为空");
        }
        productCode = productCode.trim();
        if(productCode.isEmpty()){
            throw new IllegalArgumentException("产品编码不能为空字符串或全为空格");
        }
        productCode = productCode.toUpperCase();
        if(!productCatalog.containsKey(productCode)){
            throw new IllegalArgumentException("产品编码不存在");
        }
        if(updatedProductModel == null){
            throw new IllegalArgumentException("产品信息不能为空");
        }
        if(updatedProductModel.getProductCode() == null||updatedProductModel.getProductName() == null||updatedProductModel.getUnit() == null||updatedProductModel.getBrand() == null||updatedProductModel.getSeries()== null){
            throw new IllegalArgumentException("产品信息不完整");
        }
        productCatalog.put(updatedProductModel.getProductCode(),updatedProductModel);
         }
     */


}
