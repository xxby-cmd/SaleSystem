package 封闭测试验收;

import org.junit.jupiter.api.Test;
import 独立复写测试.Customer;
import 独立复写测试.CustomerDirectory;

import static org.junit.jupiter.api.Assertions.*;

public class TestCustomer {
    @Test
    public void testRegisterSuccess(){
        Customer C=new Customer("123","zhangsan");
        CustomerDirectory CD=new CustomerDirectory(C);
        assertEquals(1,CD.size());
        assertSame(C,CD.findByCode("123"));
        assertSame(C,CD.findByCode(" 123 "));
        assertNull(CD.findByCode("13"));
    }
    @Test
    public void testRegisterReject(){
        assertThrows(IllegalArgumentException.class,()->{Customer C=new Customer("","zhangsan");});
        assertThrows(IllegalArgumentException.class,()->{Customer C=new Customer(null,"zhangsan");});
        assertThrows(IllegalArgumentException.class,()->{Customer C=new Customer("   ","zhangsan");});
        assertThrows(IllegalArgumentException.class,()->{CustomerDirectory CD=new CustomerDirectory(null);});
    }
    @Test
    public void testRegisterRejectRepeat(){
       Customer C=new Customer("123","zhangsan");
       Customer C1=new Customer("123","zhangsan");
       CustomerDirectory CD=new CustomerDirectory(C);
       assertThrows(IllegalArgumentException.class,()->{CD.register(C1);});
       assertEquals(1,CD.size());
       assertSame(C,CD.findByCode("123"));
    }
    @Test
    public void testRegisterIndependentCatalog(){
        Customer C=new Customer("123","zhangsan");
        CustomerDirectory CD=new CustomerDirectory(C);
        CustomerDirectory CD1=new CustomerDirectory();
        assertEquals(1,CD.size());
        assertSame(C,CD.findByCode("123"));
        assertEquals(0,CD1.size());
    }






}
