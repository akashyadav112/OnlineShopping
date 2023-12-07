package com.Akash.orderservice.service;

import com.Akash.orderservice.dto.InventoryResponse;
import com.Akash.orderservice.dto.OrderLineItemsDto;
import com.Akash.orderservice.dto.OrderRequest;
import com.Akash.orderservice.model.OrderLineItems;
import com.Akash.orderservice.model.Orders;
import com.Akash.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor // for doing the constructor injection of the OrderService
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository; // constructor injection done via RequiredArgsConstructor
    private final WebClient webClient; // replacement of restTemplate
    @Override
    public void placeOrder(OrderRequest orderRequest) {
        log.info("entering the placeOrder {}",orderRequest);
        Orders orders = new Orders();
        orders.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList =  orderRequest.getOrderLineItemsDto().stream()
                .map(this::mapTo)
                .toList();
        orders.setOrderLineItems(orderLineItemsList);
        log.info("the Orders Entity is  {}",orders);

        List<String> skuCodes = orders.getOrderLineItems().stream()
                .map(OrderLineItems::getSkuCode).toList();
        log.info("skuCodes are {}",skuCodes);

        InventoryResponse[] inventoryResponses = webClient.get()
                .uri("http://localhost:9091/api/inventory",uriBuilder -> uriBuilder.queryParam ("skuCode",skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class) // this type of response we will be getting and we need to read this tpye of response
                .block();  // by default the webClient is async communication so need to block it.
        log.debug("inventoryResponses are {}",inventoryResponses);

        boolean inStockOrNot = Arrays.stream(inventoryResponses).allMatch(InventoryResponse :: getIsInStock);
        if(inStockOrNot){ // means this order only when all the products are in stock or do not store it..
            orderRepository.save(orders);
            log.info("saved Successfully ");

        }else{
            log.debug("Order could not completed !!");
            throw new IllegalArgumentException("Product is not present in the inventory so order cannot be placed");
        }
    }

    private OrderLineItems mapTo(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems  = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
