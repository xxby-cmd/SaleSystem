import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TestConnection {
    String url="jdbc:mysql://127.0.0.1:3306/ProductInformation";
    String username=System.getenv("SALES_DB_USERNAME");
    String password=System.getenv("SALES_DB_PASSWORD");
    @Test
    public void testConnection() throws SQLException {
        try(Connection c = DriverManager.getConnection(url, username, password)){
            assertTrue(c.isValid(2));
        }
    }
}
