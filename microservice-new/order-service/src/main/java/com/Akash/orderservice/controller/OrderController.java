package com.Akash.orderservice.controller;

import com.Akash.orderservice.dto.OrderRequest;
import com.Akash.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/placeOrder")
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        log.info("entering the placeOrder {}",orderRequest.toString());
        orderService.placeOrder(orderRequest);
        return "order placed Successfully";
    }
}
