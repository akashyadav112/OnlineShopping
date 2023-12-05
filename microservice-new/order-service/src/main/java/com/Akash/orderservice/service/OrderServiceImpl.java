package com.Akash.orderservice.service;

import com.Akash.orderservice.dto.OrderLineItemsDto;
import com.Akash.orderservice.dto.OrderRequest;
import com.Akash.orderservice.model.OrderLineItems;
import com.Akash.orderservice.model.Orders;
import com.Akash.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor // for doing the constructor injection of the orderservice
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository; // constructor injection done via RequiredArgsConstructor
    @Override
    public void placeOrder(OrderRequest orderRequest) {
        log.debug("entering the placeOrder {}",orderRequest);
        Orders orders = new Orders();
        orders.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList =  orderRequest.getOrderLineItemsDto().stream()
                .map(this::mapTo)
                .toList();
        orders.setOrderLineItems(orderLineItemsList);
        log.debug("the Orders Entity is  {}",orders);
        orderRepository.save(orders);
    }

    private OrderLineItems mapTo(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems  = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
