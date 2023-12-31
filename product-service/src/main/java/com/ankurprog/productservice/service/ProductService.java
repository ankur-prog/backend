package com.ankurprog.productservice.service;

import com.ankurprog.productservice.dto.ProductRequest;
import com.ankurprog.productservice.dto.ProductResponse;
import com.ankurprog.productservice.model.Product;
import com.ankurprog.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(List<ProductRequest> productRequests) {

        productRequests.stream().map(productRequest -> Product.builder().productName(productRequest.getProductName()).productCategory(productRequest.getProductCategory()).description(productRequest.getDescription()).battery(productRequest.getBattery()).brand(productRequest.getBrand()).displaySize(productRequest.getDisplaySize()).price(productRequest.getPrice()).currency(productRequest.getCurrency()).faceID(productRequest.getFaceID()).touchId(productRequest.getTouchId()).exchangePossible(productRequest.getExchangePossible()).releaseDate(productRequest.getReleaseDate()).lastupdatedDate(LocalDateTime.now()).origin(productRequest.getOrigin()).build()).forEach(product -> {
            productRepository.save(product);
            log.info("product with product id {} and product name {} is created successfully.", product.getId(), product.getProductName());
        });

    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder().id(product.getId()).productCategory(product.getProductCategory()).productName(product.getProductName()).brand(product.getBrand()).exchangePossible(product.getExchangePossible()).battery(product.getBattery()).displaySize(product.getDisplaySize()).origin(product.getOrigin()).price(product.getPrice()).currency(product.getCurrency()).releaseDate(product.getReleaseDate()).faceID(product.getFaceID()).touchId(product.getTouchId()).description(product.getDescription()).build();
    }

    public List<ProductResponse> getAllProductsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return productRepository.findByReleaseDateBetween(startDate, endDate);
    }

    public List<ProductResponse> getProductByBrand(String brandName) {
        return productRepository.findByBrandIgnoreCase(brandName);
    }

    public List<ProductResponse> getProductByProductCategoryAndBrandName(String productCategory, String brandName) {

        return productRepository.findByProductCategoryAndBrandIgnoreCase(productCategory, brandName);
    }

    public boolean updatePrice(String id, Double price) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setPrice(price);
            productRepository.save(product);
            return true;
        }
        return false;

    }

    public boolean deleteProduct(String id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}