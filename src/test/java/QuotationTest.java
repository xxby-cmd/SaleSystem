import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class QuotationTest {
    private Quotation q = new Quotation(LocalDate.of(2026,8,25));
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
        Quotation q1=new Quotation(LocalDate.of(2026,8,25),qI);
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
    //过期校验
    @Test
    public void testQuotationAddSuccess(){
        assertEquals(QuotationStatus.DRAFT,q.getStatus() );
        assertTrue(q.isValidOn(LocalDate.of(2026,8,24)));
        assertTrue(q.isValidOn(LocalDate.of(2026,8,25)));
        assertFalse(q.isValidOn(LocalDate.of(2026,8,26)));
    }
    @Test
    public void testQuotationStatusDraftToConfirmedSuccess(){
        QuotationItem qI = new QuotationItem("Sau32x175",100,new BigDecimal("175.00"));
        q.addItem(qI);
        assertEquals(1,q.getQuantity());
        q.confirmStatus(LocalDate.of(2026,8,24));
        assertEquals(QuotationStatus.CONFIRMED,q.getStatus());
        assertThrows(IllegalStateException.class,()->q.addItem(qI));
        assertThrows(IllegalStateException.class,()->q.removeItemByProductCode("SAU32x175"));
        assertEquals(1,q.getQuantity());
        assertEquals(new BigDecimal("17500.00"),q.getTotalPrice());
    }
    //拒绝重复确认
    @Test
    public void testQuotationStatusConfirmedToConfirmedSuccess(){
        QuotationItem qI = new QuotationItem("Sau32x175",100,new BigDecimal("175.00"));
        Quotation q1=new Quotation(LocalDate.of(2026,8,24));
        q1.addItem(qI);
        q1.confirmStatus(LocalDate.of(2026,8,24));
        assertEquals(QuotationStatus.CONFIRMED,q1.getStatus());
        assertThrows(IllegalArgumentException.class,()->q1.confirmStatus(LocalDate.of(2026,8,24)));
    }

    //拒绝过期
    @Test
    public void testQuotationStatusDraftToConfirmedFailure1(){
        QuotationItem qI = new QuotationItem("Sau32x175",100,new BigDecimal("175.00"));
        Quotation q1=new Quotation(LocalDate.of(2026,8,24));
        q1.addItem(qI);
        assertThrows(IllegalArgumentException.class,()->q1.confirmStatus(LocalDate.of(2026,8,26)));
        assertEquals(QuotationStatus.DRAFT,q1.getStatus());


    }
    //拒绝空值确认
    @Test
    public void testQuotationStatusDraftToConfirmedFailure2(){
        Quotation q1=new Quotation(LocalDate.of(2026,8,24));
        assertThrows(IllegalArgumentException.class,()->q1.confirmStatus(LocalDate.of(2026,8,23)));
        assertEquals(QuotationStatus.DRAFT,q1.getStatus());
    }
    //拒绝空日期确认
    @Test
    public void testQuotationStatusDraftToConfirmedFailure3(){
        Quotation q1=new Quotation(LocalDate.of(2026,8,24));
        QuotationItem qI = new QuotationItem("Sau32x175",100,new BigDecimal("175.00"));
        q1.addItem(qI);
        assertThrows(IllegalArgumentException.class,()->q1.confirmStatus(null));
        assertEquals(QuotationStatus.DRAFT,q1.getStatus());
    }
}
