import com.xxby.File.LoadByFile;
import com.xxby.Product.ProductCatalog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;


public class TestFile {
    Path filePath = Path.of("src/test/com.xxby.Product.ProductModel.csv");
    ProductCatalog productCatalog=new ProductCatalog();

    @Test
    public void testFileLoaderSuccess(@TempDir Path tempDir) throws IOException {
        Path tempFilePath = tempDir.resolve("com.xxby.Product.ProductModel.csv");
        Files.writeString(tempFilePath, """
                productCode,productName,Brand,Series,Unit
                SAU32X100,StandardCylinder,FESTO,SC,COUNT
                SDA20X50,StandardCylinder,SMC,SDA,COUNT""");
        LoadByFile lBF = new LoadByFile();
        productCatalog = lBF.fileLoader(tempFilePath);
        assertEquals(2,productCatalog.size());
    }
    @Test
    public void testFileLoaderUnicodeSuccess(@TempDir Path tempDir) throws IOException {
        Path tempFilePath = tempDir.resolve("com.xxby.Product.ProductModel.csv");
        Files.writeString(tempFilePath, """
                \uFEFFproductCode,productName,Brand,Series,Unit
                SAU32X100,StandardCylinder,FESTO,SC,COUNT
                SDA20X50,StandardCylinder,SMC,SDA,COUNT""");
        LoadByFile lBF = new LoadByFile();
        productCatalog = lBF.fileLoader(tempFilePath);
        assertEquals(2,productCatalog.size());
    }
    @Test
    public void testFileNotFound() throws IOException {
        LoadByFile lBF = new LoadByFile();
        assertThrows(IOException.class,()->productCatalog = lBF.fileLoader(Path.of("src/test/NotFound.csv")));
    }
    @Test
    public void testLoadByFileIndependence(@TempDir Path tempDir) throws IOException {
        Path tempFilePath = tempDir.resolve("com.xxby.Product.ProductModel.csv");
        Files.writeString(tempFilePath, """
                productCode,productName,Brand,Series,Unit
                SAU32X100,StandardCylinder,FESTO,SC,COUNT
                SDA20X50,StandardCylinder,SMC,SDA,COUNT""");
        LoadByFile lBF = new LoadByFile();
        productCatalog = lBF.fileLoader(tempFilePath);
        assertEquals(2,productCatalog.size());
        ProductCatalog productCatalog2 = new ProductCatalog();
        productCatalog2 = lBF.fileLoader(tempFilePath);
        assertNotEquals(productCatalog, productCatalog2);
        assertEquals(2,productCatalog2.size());
    }
    @Test
    public void testLoadByFileRejectEmptyFifthColumn(@TempDir Path tempDir) throws IOException {
        Path tempFilePath = tempDir.resolve("com.xxby.Product.ProductModel.csv");
        Files.writeString(tempFilePath, """
                productCode,productName,Brand,Series,Unit
                SAU32X100,StandardCylinder,FESTO,SC,COUNT
                SDA20X50,StandardCylinder,SMC,SDA,""");
        LoadByFile lBF = new LoadByFile();
        assertThrows(IllegalArgumentException.class,()->productCatalog = lBF.fileLoader(tempFilePath));
    }
    @Test
    public void testLoadByFileRejectErrorEnum(@TempDir Path tempDir) throws IOException {
        Path tempFilePath = tempDir.resolve("com.xxby.Product.ProductModel.csv");
        Files.writeString(tempFilePath, """
                productCode,productName,Brand,Series,Unit
                SAU32X100,StandardCylinder,FESTO,SC,COUNT
                SDA20X50,StandardCylinder,SMC,SDA,COOU""");
        LoadByFile lBF = new LoadByFile();
        assertThrows(IllegalArgumentException.class,()->productCatalog = lBF.fileLoader(tempFilePath));
    }
    @Test
    public void testLoadByFileRejectRepeatProductCode(@TempDir Path tempDir) throws IOException {
        Path tempFilePath = tempDir.resolve("com.xxby.Product.ProductModel.csv");
        Files.writeString(tempFilePath, """
                productCode,productName,Brand,Series,Unit
                SAU32X100,StandardCylinder,FESTO,SC,COUNT
                SDA20X50,StandardCylinder,SMC,SDA,COUNT
                SDA20X50,StandardCylinder,SMC,SDA,COUNT""");
        LoadByFile lBF = new LoadByFile();
        assertThrows(IllegalArgumentException.class,()->productCatalog = lBF.fileLoader(tempFilePath));
    }
    @Test
    public void testLoadByFileRejectErrorHead(@TempDir Path tempDir) throws IOException {
        Path tempFilePath = tempDir.resolve("com.xxby.Product.ProductModel.csv");
        Files.writeString(tempFilePath, """
                productCo,productName,Brand,Series,Unit
                SAU32X100,StandardCylinder,FESTO,SC,COUNT
                SDA20X50,StandardCylinder,SMC,SDA,COUNT""");
        LoadByFile lBF = new LoadByFile();
        assertThrows(IllegalArgumentException.class,()->productCatalog = lBF.fileLoader(tempFilePath));
    }
    @Test
    public void testLoadByFileRejectErrorHead2(@TempDir Path tempDir) throws IOException {
        Path tempFilePath = tempDir.resolve("com.xxby.Product.ProductModel.csv");
        Files.writeString(tempFilePath, """
                productCode,productNa,Brand,Series,Unit
                SAU32X100,StandardCylinder,FESTO,SC,COUNT
                SDA20X50,StandardCylinder,SMC,SDA,COUNT""");
        LoadByFile lBF = new LoadByFile();
        assertThrows(IllegalArgumentException.class,()->productCatalog = lBF.fileLoader(tempFilePath));
    }
    @Test
    public void testLoadByFileRejectEmptyFile(@TempDir Path tempDir) throws IOException {
        Path tempFilePath = tempDir.resolve("com.xxby.Product.ProductModel.csv");
        Files.createFile(tempFilePath);
        LoadByFile lBF = new LoadByFile();
        assertThrows(IllegalArgumentException.class,()->productCatalog = lBF.fileLoader(tempFilePath));
    }
    @Test
    public void testLoadByFileOnlyEHead(@TempDir Path tempDir) throws IOException {
        Path tempFilePath = tempDir.resolve("com.xxby.Product.ProductModel.csv");
        Files.writeString(tempFilePath, """
                productCode,productName,Brand,Series,Unit
                """);
        LoadByFile lBF = new LoadByFile();
        assertTrue(lBF.fileLoader(tempFilePath).size()==0);
    }
}
