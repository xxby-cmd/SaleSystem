package com.xxby.Product;

import com.xxby.Exception.ProductNotFoundException;
import com.xxby.web.ProductCatalogCreateRequest;
import com.xxby.web.ProductCreateRequest;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class ProductCatalogService {

        private final ProductCatalogRepository productCatalogRepository;

        public ProductCatalogService(ProductCatalogRepository productCatalogRepository) {
                this.productCatalogRepository = productCatalogRepository;
        }

        public void addProductCatalog(ProductCatalogCreateRequest request) {
                ProductCatalog productCatalog = new ProductCatalog(request.productCatalogName());
                for(ProductCreateRequest productCreateRequest:request.productCatalog()){
                        ProductModel temp= new ProductModel(
                                productCreateRequest.productCode(),
                                productCreateRequest.productName(),
                                productCreateRequest.brand(),
                                productCreateRequest.unit()
                                );
                        productCatalog.addProductModel(temp);
                }
                productCatalogRepository.save(productCatalog);
        }
        public ProductModel findByProductCode(String productCode) {
                return productCatalogRepository.findByProductCode(productCode).orElseThrow(()->new ProductNotFoundException("产品“"+productCode+"”不存在"));
        }
        
}
