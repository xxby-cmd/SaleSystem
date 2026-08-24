import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    private Quotation quotation=new Quotation(LocalDate.of(2026,8,26),new QuotationItem("SAC32x100",1, BigDecimal.valueOf(100)));

    @Test
    public void testSingleTransformSuccess() {
        quotation.confirmStatus(LocalDate.of(2026,8,25));
        OrderDraft orderDraft = OrderDraft.from(quotation);
        assertEquals(1,orderDraft.getOrders().size());
        assertEquals(BigDecimal.valueOf(100),orderDraft.getTotal());
    }
    @Test
    public void testMultipleTransformSuccess() {
        quotation.addItem(new QuotationItem("SA32x100",1, BigDecimal.valueOf(100)));
        quotation.addItem(new QuotationItem("SC32x100",1, BigDecimal.valueOf(100)));
        quotation.addItem(new QuotationItem("AC32x100",1, BigDecimal.valueOf(100)));
        quotation.addItem(new QuotationItem("SAd32x100",1, BigDecimal.valueOf(100)));
        quotation.confirmStatus(LocalDate.of(2026,8,25));
        assertEquals(5,quotation.getItems().size());
        assertEquals(BigDecimal.valueOf(500),quotation.getTotalPrice());
        OrderDraft orderDraft = OrderDraft.from(quotation);
        assertEquals(5,orderDraft.getOrders().size());
        assertEquals(BigDecimal.valueOf(500),orderDraft.getTotal());
    }
    @Test
    public void testTransformGiftSuccess() {
        Quotation q=new Quotation(LocalDate.of(2026,8,26),new QuotationItem("SAC32x100",1, BigDecimal.valueOf(0)));
        q.confirmStatus(LocalDate.of(2026,8,25));
        assertEquals(1,OrderDraft.from(q).getOrders().size());
        assertEquals(BigDecimal.valueOf(0),OrderDraft.from(q).getTotal());
    }
    @Test
    public void testTransformNullFail() {
        assertThrows(NullPointerException.class,()-> OrderDraft.from(null));
    }
    @Test
    public void testTransformDraftFail() {
        assertThrows(IllegalStateException.class,()-> OrderDraft.from(quotation));
    }
    @Test
    public void testFixedQuotation() {
        quotation.confirmStatus(LocalDate.of(2026,8,25));
        OrderDraft orderDraft = OrderDraft.from(quotation);
        assertEquals(1,orderDraft.getOrders().size());
        assertEquals(BigDecimal.valueOf(100),orderDraft.getTotal());
        quotation.getItems().getFirst().setPrice(BigDecimal.valueOf(200));
        assertEquals(BigDecimal.valueOf(200),quotation.getTotalPrice());
        assertEquals(BigDecimal.valueOf(100),orderDraft.getTotal());
    }
    @Test
    public void testFixedQuotation1() {
        quotation.confirmStatus(LocalDate.of(2026,8,25));
        OrderDraft orderDraft = OrderDraft.from(quotation);
        assertThrows(UnsupportedOperationException.class,()-> orderDraft.getOrders().add(new OrderItem(quotation.getItems().getFirst())));
        assertThrows(UnsupportedOperationException.class,()-> orderDraft.getOrders().removeFirst());
    }


}
