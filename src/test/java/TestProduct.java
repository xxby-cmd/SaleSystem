import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestProduct {
    @Test
    public void testProductModel() {
        ProductModel productModel = new ProductModel();
        productModel.setProductCode("Sau32x150");
        assertEquals("SAU32X150",productModel.getProductCode());

        assertThrows(IllegalArgumentException.class,()-> new ProductModel("Sau32x150"));}
    @Test
    public void testProductInfo() {
        QuotationItem qI = new QuotationItem("Sau32x175",100,new BigDecimal("175.00"));
        assertEquals(new BigDecimal("17500.00"),qI.getSum());
    }
    @Test
    public void testProductZeroPrice() {
        QuotationItem qI = new QuotationItem("Sau32x175",4,BigDecimal.ZERO);
        assertEquals(0,qI.getSum().compareTo(BigDecimal.ZERO));
    }
    @Test
    public void testProductQuantity() {
        assertThrows(IllegalArgumentException.class,()-> {QuotationItem qI = new QuotationItem("Sau32x175",0,BigDecimal.ZERO);});
        assertThrows(IllegalArgumentException.class,()-> {QuotationItem qI = new QuotationItem("Sau32x175",-1,BigDecimal.ZERO);});
    }
    @Test
    public void testProductPrice() {
        assertThrows(IllegalArgumentException.class,()-> {QuotationItem qI = new QuotationItem("Sau32x175",8,new BigDecimal("-100.00"));});
        assertThrows(IllegalArgumentException.class,()-> {QuotationItem qI = new QuotationItem("Sau32x175",4,null);});
    }

}


