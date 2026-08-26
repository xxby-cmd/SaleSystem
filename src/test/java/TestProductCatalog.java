import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class TestProductCatalog {
    ProductModel productModel = new ProductModel("Sau32x150", ProductInfo.ProductName.StandardCylinder, ProductInfo.Brand.FESTO, ProductInfo.Series.CDJ2B, ProductInfo.Unit.METER);
    ProductCatalog productCatalog = new ProductCatalog();
    @Test
    public void testAddProductCatalog() {
        productCatalog.addProductModel(productModel);
        assertEquals(1, productCatalog.size());
    }
    @Test
    public void testFindByProductCode() {
        productCatalog.addProductModel(productModel);
        assertEquals(productModel, productCatalog.findByProductCode(productModel.getProductCode()));
        assertEquals(productModel.getUnit(), productCatalog.findByProductCode("Sau32x150").getUnit());
    }
    @Test
    public void testContainsCode() {
        productCatalog.addProductModel(productModel);
        assertTrue(productCatalog.containsCode("Sau32x150"));
        assertFalse(productCatalog.containsCode("Sau32x1501"));
    }
    @Test
    public void testRejectRepeatProductModel() {
        productCatalog.addProductModel(productModel);
        assertThrows(IllegalArgumentException.class,(() -> productCatalog.addProductModel(new ProductModel("SaU32X150", ProductInfo.ProductName.UnStandardCylinder, ProductInfo.Brand.FESTO, ProductInfo.Series.CDJ2B, ProductInfo.Unit.METER))));
        assertEquals(1, productCatalog.size());
        assertEquals(productModel, productCatalog.findByProductCode(productModel.getProductCode()));
    }
    @Test
    public void testRejectNullProductModel() {
        assertThrows(NullPointerException.class,(() -> productCatalog.addProductModel(null)));
        assertEquals(0, productCatalog.size());
    }
    @Test
    public void testRejectUnknownProductModel() {
        assertThrows(IllegalArgumentException.class,(() -> productCatalog.addProductModel(new ProductModel("Sau32x150", ProductInfo.ProductName.StandardCylinder, ProductInfo.Brand.FESTO, null,null))));
        assertEquals(0, productCatalog.size());
    }
    @Test
    public void testRejectUnknownFindByProductCode() {
        assertThrows(IllegalArgumentException.class,(() -> productCatalog.findByProductCode(null)));
        assertThrows(IllegalArgumentException.class,(() -> productCatalog.findByProductCode("")));
        assertThrows(IllegalArgumentException.class,(() -> productCatalog.findByProductCode("   ")));
    }
    @Test
    public void testFindByProductCode1() {
        productCatalog.addProductModel(productModel);
        assertEquals(productModel, productCatalog.findByProductCode("Sau32x150"));
        assertNull(productCatalog.findByProductCode("Sau32x1501"));
    }

}
