import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class QuotationTest {
    private Quotation q = new Quotation();
    //新报价单数量为0
    @Test
    public void testQuotationQuantity(){
        assertEquals(0,q.getQuantity());
    }
    //空报价总额为0
    @Test
    public void testQuotationTotal(){
        assertEquals(BigDecimal.ZERO,q.getTotalPrice());
    }
    //添加一条明细成功
    @Test
    public void testQuotationAddSingleItem(){
        QuotationItem qI = new QuotationItem("Sau32x175",100,new BigDecimal("175.00"));
        q.addItem(qI);
        assertEquals(1,q.getQuantity());
    }
    //添加多条明细成功
    @Test
    public void testQuotationAddMultipleItems(){
        q.addItem(new QuotationItem("Sau32x175",100,new BigDecimal("175.00")));
        q.addItem(new QuotationItem("Sau30x170",100,new BigDecimal("175.00")));
        q.addItem(new QuotationItem("Sa32x175",100,new BigDecimal("175.00")));
        q.addItem(new QuotationItem("Sac30x170",100,new BigDecimal("175.00")));
        q.addItem(new QuotationItem("SaD32x175",100,new BigDecimal("175.00")));
        q.addItem(new QuotationItem("SaE30x170",100,new BigDecimal("175.00")));
        assertEquals(6,q.getQuantity());
    }
    //一条明细金额正确
    @Test
    public void testQuotationSingleItemPriceRight(){
        q.addItem(new QuotationItem("Sau32x175",100,new BigDecimal("175.00")));
        assertEquals(new BigDecimal("17500.00"),q.getTotalPrice());
    }
    //多条明细金额正确
    @Test
    public void testQuotationMultipleItemsPriceRight(){
        QuotationItem qI = new QuotationItem("Sau32x175",100,new BigDecimal("175.00"));
        QuotationItem qI2 =new QuotationItem("Sa32x175",50,new BigDecimal("175.00"));
        QuotationItem qI3 =new QuotationItem("Su32x175",100,new BigDecimal("110.00"));
        QuotationItem qI4 =new QuotationItem("au32x175",100,new BigDecimal("180.00"));
        q.addItem(qI);
        q.addItem(qI2);
        q.addItem(qI3);
        q.addItem(qI4);
        assertEquals(new BigDecimal("55250.00"),q.getTotalPrice());
    }
    //赠品不影响总额
    @Test
    public void testQuotationGift(){
        QuotationItem qI = new QuotationItem("Sau32x175",100,new BigDecimal("175.00"));
        q.addItem(qI);
        assertEquals(new BigDecimal("17500.00"),q.getTotalPrice());
        QuotationItem qI2=new QuotationItem("S32x175",100,new BigDecimal("0.00"));
        q.addItem(qI2);
        assertEquals(new BigDecimal("17500.00"),q.getTotalPrice());
    }
    //拒绝null
    @Test
    public void testQuotationRejectNull(){
        assertThrows(NullPointerException.class, () -> q.addItem(null));
        assertEquals(0,q.getQuantity());
        assertEquals(BigDecimal.ZERO,q.getTotalPrice());
    }
    //拒绝不完整明细
    @Test
    public void testQuotationRejectIncomplete(){
        QuotationItem qI=new QuotationItem();
        qI.setProductCode("Sa32x175");
        assertThrows(IllegalArgumentException.class, () -> q.addItem(qI));
        assertEquals(0,q.getQuantity());
        assertEquals(BigDecimal.ZERO,q.getTotalPrice());

        qI.setQuantity(100);
        assertThrows(IllegalArgumentException.class, () -> q.addItem(qI));
        assertEquals(0,q.getQuantity());
        assertEquals(BigDecimal.ZERO,q.getTotalPrice());

        QuotationItem qI2=new QuotationItem();
        qI2.setPrice(new BigDecimal("175.00"));
        assertThrows(IllegalArgumentException.class, () -> q.addItem(qI2));
        assertEquals(0,q.getQuantity());
        assertEquals(BigDecimal.ZERO,q.getTotalPrice());

        qI.setPrice(new BigDecimal("175.00"));
        q.addItem(qI);
        assertEquals(1,q.getQuantity());
        assertEquals(new BigDecimal("17500.00"),q.getTotalPrice());

    }
    //拒绝重复产品与重复失败后状态不变
    @Test
    public void testQuotationRejectDuplicate(){

        QuotationItem qI = new QuotationItem("Sau32x175",100,new BigDecimal("175.00"));
        Quotation q1=new Quotation(qI);
        assertEquals(1,q1.getQuantity());
        assertThrows(IllegalArgumentException.class, () -> q1.addItem(new QuotationItem("Sau32x175",100,new BigDecimal("100.00"))));
        assertEquals(1,q1.getQuantity());
        assertEquals(qI.getSum(),q1.getTotalPrice());
    }
    //外部不能修改内部列表
    @Test
    public void testQuotationRejectExternal(){
        QuotationItem qI = new QuotationItem("Sau32x175",100,new BigDecimal("175.00"));
        q.addItem(new QuotationItem("Sa32x175",100,new BigDecimal("100.00")));
        assertThrows(UnsupportedOperationException.class,()->{q.getItems().add(qI);});
        assertEquals(1,q.getQuantity());
    }
    //删除产品编码
    @Test
    public void testQuotationDeleteSuccess(){
        QuotationItem qI = new QuotationItem("Sau32x175",100,new BigDecimal("175.00"));
        q.addItem(qI);
        assertEquals(1,q.getQuantity());
        assertTrue(q.removeItemByProductCode("SAU32x175"));
        assertEquals(0, q.getQuantity());
        assertEquals(BigDecimal.ZERO,q.getTotalPrice());

    }
    @Test
    public void testQuotationDeleteFailure(){
        QuotationItem qI = new QuotationItem("Sau32x175",100,new BigDecimal("175.00"));
        q.addItem(qI);
        assertEquals(1,q.getQuantity());
        assertFalse(q.removeItemByProductCode("SA32x175"));
        assertEquals(1,q.getQuantity());
        assertEquals(qI.getSum(),q.getTotalPrice());

    }
}
