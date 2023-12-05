package com.Akash.orderservice.service;

import com.Akash.orderservice.dto.OrderRequest;

public interface OrderService {
    public void placeOrder(OrderRequest orderRequest);
}
