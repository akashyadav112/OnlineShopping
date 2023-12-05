package com.Akash.productservice.service.impl;

import com.Akash.productservice.dto.ProductRequest;
import com.Akash.productservice.dto.ProductResponse;
import com.Akash.productservice.models.Products;
import com.Akash.productservice.repository.ProductRepository;
import com.Akash.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // for logging
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository; // instead of intializing this in the constructor, we have used that RequriedArgsConstructor
    @Override
    public void createProduct(ProductRequest productRequest) {
        // converting from dto to entity ( ModelMapper we can use when the no of the attributes are high)
        Products product = Products.builder().name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Products> products = productRepository.findAll();
        return products.stream().map(this :: mapToProductResponse).toList();
    }

    public ProductResponse mapToProductResponse(Products product){
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
        return productResponse;
    }
}
