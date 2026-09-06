package com.xxby.Product;

import java.util.Optional;

public interface ProductCatalogRepository {

    void save(ProductCatalog productCatalog) ;

    Optional<ProductModel> findByProductCode(String productCode) ;

}
