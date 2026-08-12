import java.math.BigDecimal;
import java.util.ArrayList;

public class ProductValidUtil {
    //工具类不能创建对象
    private ProductValidUtil() {}
    //过滤产品名称
    public static void validProductCode(String productCode) {
        if(productCode ==null|| productCode.isEmpty()){
            throw new IllegalArgumentException("产品编码不能为空");
        }else if(productCode.isBlank()){
            throw new IllegalArgumentException("产品编码不能为纯空格");
        }
    }
    public static void validProductCode(String ProductCode,ArrayList<ProductModel> productList) {
        for(ProductModel item:productList){
            if(item.getProductCode().equals(ProductCode)){
                throw new IllegalArgumentException("产品编码已存在");
            }
        }
    }

    public static void validQuantity(int quantity) {
        if(quantity<=0){
            throw new IllegalArgumentException("数量必须大于0");
        }

    }

    public static void validPrice(BigDecimal price) {
        if(price==null){
            throw new IllegalArgumentException("价格不能为空");
        }
        else if(price.compareTo(BigDecimal.ZERO)<0){
            throw new IllegalArgumentException("价格必须不能小于0");
        }

    }


}
