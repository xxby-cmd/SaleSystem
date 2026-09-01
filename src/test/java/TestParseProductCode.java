import com.xxby.Product.ParseProductCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestParseProductCode {
    @Test
    public void testParseValidProductCodeSuccess() {
        String productCode = "SAS12X456J";
        ParseProductCode parser = new ParseProductCode(productCode);
        assertEquals(productCode, parser.getProductCode());
        assertEquals("SAS", parser.getSeries());
        assertEquals("12", parser.getBoreDiameter());
        assertEquals("456", parser.getStrokeLength());
        assertEquals("J", parser.getSuffix());
    }
    @Test
    public void testParseValidProductCodeIgnoreCase() {
        String productCode = "  Sas12X456J ";
        ParseProductCode parser = new ParseProductCode(productCode);
        assertEquals("SAS12X456J", parser.getProductCode());
        assertEquals("SAS", parser.getSeries());
        assertEquals("12", parser.getBoreDiameter());
        assertEquals("J", parser.getSuffix());
        assertEquals("456", parser.getStrokeLength());
    }
    @Test
    //* 测试解析商品编码时，商品编码中不包含后缀
    public void testParseValidProductCodeNoSuffix() {
        String productCode = "  Sas12X456 ";
        ParseProductCode parser = new ParseProductCode(productCode);
        assertEquals("SAS12X456", parser.getProductCode());
        assertEquals("SAS", parser.getSeries());
        assertEquals("12", parser.getBoreDiameter());
        assertNull(parser.getSuffix());
        assertEquals("456", parser.getStrokeLength());
    }
    @Test
    public void testParseProductCodeRejectInvalid() {
        String productCode = "Sas12 s456 ";
        assertThrows(IllegalArgumentException.class, () -> new ParseProductCode(productCode));
    }
}
