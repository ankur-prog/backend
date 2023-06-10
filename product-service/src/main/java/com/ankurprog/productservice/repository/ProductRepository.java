package com.ankurprog.productservice.repository;

import com.ankurprog.productservice.dto.ProductResponse;
import com.ankurprog.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product,String> {

    List<ProductResponse> findByReleaseDateBetween(LocalDate startDate, LocalDate endDate);

    List<ProductResponse> findByBrandIgnoreCase(String brandName);

    List<ProductResponse> findByProductCategoryAndBrandIgnoreCase(String productCategory, String brandName);


}
