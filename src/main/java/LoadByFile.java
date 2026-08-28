import java.io.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class LoadByFile {

    public ProductCatalog fileLoader(Path path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile(), StandardCharsets.UTF_8))) {
            ProductCatalog productCatalog = new ProductCatalog();
            int lineNumber = 0;
            int COLUMN = 5;
            //判断表头是否合法
            String line = reader.readLine();
            lineNumber++;
            if (line == null) {
                throw new IllegalArgumentException("文件为空,表头不存在");
            }
            String[] heads = line.split(",", -1);

            if (!(heads.length == COLUMN &&
                    (heads[0].equalsIgnoreCase("productCode")||
                    heads[0].equalsIgnoreCase("\uFEFFproductCode")) &&
                    heads[1].equalsIgnoreCase("productName")&&
                    heads[2].equalsIgnoreCase("brand") &&
                    heads[3].equalsIgnoreCase("series") &&
                    heads[4].equalsIgnoreCase("unit"))) {
                throw new IllegalArgumentException("表头商品目录字段出现错误");
            }

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] items = line.split(",", -1);
                if (items.length != COLUMN) {
                    throw new IllegalArgumentException("第" + lineNumber + "行预期是" + COLUMN + "列"+"，实际上是"+items.length+"列");
                }
                ProductModel product;
                try {
                    product=new ProductModel(items[0]);
                }catch (IllegalArgumentException e){
                    throw new IllegalArgumentException("第" + lineNumber + "行的第1列：商品编码: "+items[0]+" 出现错误" + e.getMessage(),e);
                }
                try {
                    product.setProductName(Enum.valueOf(ProductInfo.ProductName.class, items[1].trim()));
                }catch (IllegalArgumentException e){
                    throw new IllegalArgumentException("第" + lineNumber + "行的第2列：商品名称: "+items[1]+" 出现错误" + e.getMessage(),e);
                }
                try {
                    product.setBrand(Enum.valueOf(ProductInfo.Brand.class, items[2].trim()));
                }catch (IllegalArgumentException e){
                    throw new IllegalArgumentException("第" + lineNumber + "行的第3列：商品品牌: "+items[2]+" 出现错误" + e.getMessage(),e);
                }
                try {
                    product.setSeries(Enum.valueOf(ProductInfo.Series.class, items[3].trim()));
                }catch (IllegalArgumentException e){
                    throw new IllegalArgumentException("第" + lineNumber + "行的第4列：商品系列: "+items[3]+" 出现错误" + e.getMessage(),e);
                }
                try {
                    product.setUnit(Enum.valueOf(ProductInfo.Unit.class, items[4].trim()));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("第" + lineNumber + "行的第5列：商品单位: "+items[4]+" 出现错误" + e.getMessage(),e);
                }

                //判断商品编码是否重复
                if (productCatalog.containsCode(product.getProductCode())) {
                    throw new IllegalArgumentException("第" + lineNumber + "行的商品编码: "+product.getProductCode()+" 已经存在");
                }


                productCatalog.addProductModel(product);

            }

            return productCatalog;
        }

    }
}
