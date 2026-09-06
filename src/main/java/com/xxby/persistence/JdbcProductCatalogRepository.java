package com.xxby.persistence;

import com.xxby.Exception.DatabaseException;
import com.xxby.Product.ProductCatalog;
import com.xxby.Product.ProductCatalogRepository;
import com.xxby.Product.ProductInfo;
import com.xxby.Product.ProductModel;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

import static com.xxby.校验规则.ValidationUtil.validProductCode;

@Repository
public class JdbcProductCatalogRepository implements ProductCatalogRepository {

    private final DataSource dataSource;

    public JdbcProductCatalogRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(ProductCatalog productCatalog) {

        String sql="INSERT INTO Total (product_Code,product_Name,brand,series,unit,bore_Diameter,stroke_Length,suffix) VALUES (?,?,?,?,?,?,?,?)";

        try(Connection connection= dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
            if(productCatalog==null){
                throw new NullPointerException("Product Catalog is null");
            }
            productCatalog.forEach((productCode,productModel)->{
                try{
                    statement.setString(1, productModel.getProductCode());
                    statement.setString(2, productModel.getProductName().name());
                    statement.setString(3, productModel.getBrand().name());
                    statement.setString(4, productModel.getSeries().name());
                    statement.setString(5, productModel.getUnit().name());
                    statement.setInt(6, productModel.getBoreDiameter());
                    statement.setInt(7, productModel.getStrokeLength());
                    if(productModel.getSuffix()!=null){
                        statement.setString(8, productModel.getSuffix().name());
                    }else{
                        statement.setNull(8, Types.VARCHAR);
                    }
                    statement.executeUpdate();
                }
                catch (SQLException e){
                    throw new DatabaseException("数据库存入失败",e);
                }
            });
        }catch (SQLException e){
            throw new DatabaseException("数据库存入失败",e);
        }
    }

    @Override
    public Optional<ProductModel> findByProductCode(String productCode) {
        validProductCode(productCode);
        productCode=productCode.toUpperCase().trim();
        String sql="SELECT * FROM Total WHERE product_Code=?";

        try(Connection connection= dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)) {
            statement.setString(1, productCode);
            ResultSet resultSet= statement.executeQuery();
            if(resultSet.next()){
                ProductModel productModel=new ProductModel(
                        productCode,
                        Enum.valueOf(ProductInfo.ProductName.class, resultSet.getString("product_Name")),
                        Enum.valueOf(ProductInfo.Brand.class, resultSet.getString("brand")),
                        Enum.valueOf(ProductInfo.Unit.class, resultSet.getString("unit"))
                );
                return Optional.of(productModel);
            }
        }catch (SQLException e){
            throw new DatabaseException("数据库查询失败",e);
        }
        return Optional.empty();



    }

}


