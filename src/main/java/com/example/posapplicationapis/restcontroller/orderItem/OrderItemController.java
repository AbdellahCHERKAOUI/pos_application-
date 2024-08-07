package com.example.posapplicationapis.restcontroller.orderItem;

import com.example.posapplicationapis.dto.orderItem.OrderItemDtoRequest;
import com.example.posapplicationapis.dto.orderItem.OrderItemDtoResponse;
import com.example.posapplicationapis.services.orderItem.OrderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/order-item")
public class OrderItemController {
    private OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }
    @PostMapping(value = "/add-item")
    public ResponseEntity<OrderItemDtoResponse> createOrderItem(@RequestBody OrderItemDtoRequest orderItemDtoRequest) {
        OrderItemDtoResponse response = orderItemService.createOrderItem(orderItemDtoRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get-order-item/{id}")
    public OrderItemDtoResponse getOrderItem(@PathVariable Long id) {
        return orderItemService.getOrderItem(id);
    }

    @GetMapping(value = "/get-all-order-items")
    public List<OrderItemDtoResponse> getAllOrderItems() {
        return orderItemService.getAllOrderItems();
    }

    @PutMapping("/update-order-item/{id}")
    public OrderItemDtoResponse updateOrderItem(@PathVariable Long id, @RequestBody OrderItemDtoRequest requestDto) {
        return orderItemService.updateOrderItem(id, requestDto);
    }

    @DeleteMapping("/delete-order-item/{id}")
    public void deleteOrderItem(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
    }

}
