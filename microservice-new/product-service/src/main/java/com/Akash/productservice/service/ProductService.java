package com.Akash.productservice.service;

import com.Akash.productservice.dto.ProductRequest;
import com.Akash.productservice.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    public void createProduct(ProductRequest productProduct);

    public List<ProductResponse> getAllProducts();
}
