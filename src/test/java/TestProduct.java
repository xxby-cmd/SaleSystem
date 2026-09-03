import com.xxby.Product.ProductInfo;
import com.xxby.Product.ProductModel;
import com.xxby.Quotation.QuotationItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestProduct {
    @Test
    public void testProductModel() {
        ProductModel productModel = new ProductModel("Sau32x150", ProductInfo.ProductName.StandardCylinder, ProductInfo.Brand.FESTO, ProductInfo.Unit.METER);
        assertEquals("SAU32X150", productModel.getProductCode());
        }
        @Test
        public void testProductInfo () {
            QuotationItem qI = new QuotationItem("Sau32x175", 100, new BigDecimal("175.00"));
            assertEquals(new BigDecimal("17500.00"), qI.getSum());
        }
        @Test
        public void testProductZeroPrice () {
            QuotationItem qI = new QuotationItem("Sau32x175", 4, BigDecimal.ZERO);
            assertEquals(0, qI.getSum().compareTo(BigDecimal.ZERO));
        }
        @Test
        public void testProductQuantity () {
            assertThrows(IllegalArgumentException.class, () -> {
                QuotationItem qI = new QuotationItem("Sau32x175", 0, BigDecimal.ZERO);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                QuotationItem qI = new QuotationItem("Sau32x175", -1, BigDecimal.ZERO);
            });
        }
        @Test
        public void testProductPrice () {
            assertThrows(IllegalArgumentException.class, () -> {
                QuotationItem qI = new QuotationItem("Sau32x175", 8, new BigDecimal("-100.00"));
            });
            assertThrows(NullPointerException.class, () -> {
                QuotationItem qI = new QuotationItem("Sau32x175", 4, null);
            });
        }
        @Test
        public void testProductModelEnum () {
            ProductModel pM = new ProductModel();
            pM.setBrand(ProductInfo.Brand.FESTO);
            pM.setUnit(ProductInfo.Unit.METER);
            assertEquals(ProductInfo.Unit.METER, pM.getUnit());
            assertEquals("米", pM.getUnitCn());
        }
    }
