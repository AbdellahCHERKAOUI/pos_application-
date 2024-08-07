package com.example.posapplicationapis.restcontroller.order;

import com.example.posapplicationapis.dto.order.OrderDtoRequest;
import com.example.posapplicationapis.dto.order.OrderDtoResponse;
import com.example.posapplicationapis.services.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/order")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/add-order")
    public ResponseEntity<OrderDtoResponse> createOrder(@RequestBody OrderDtoRequest orderDtoRequest) {
        OrderDtoResponse orderDtoResponse = orderService.createOrder(orderDtoRequest);
        return ResponseEntity.ok(orderDtoResponse);
    }
    @GetMapping("/get-order/{id}")
    public ResponseEntity<OrderDtoResponse> getOrder(@PathVariable Long id) {
        OrderDtoResponse orderDtoResponse = orderService.getOrder(id);
        return ResponseEntity.ok(orderDtoResponse);
    }

    @GetMapping("/get-all-order")
    public ResponseEntity<List<OrderDtoResponse>> getAllOrders() {
        List<OrderDtoResponse> orderDtoResponses = orderService.getAllOrders();
        return ResponseEntity.ok(orderDtoResponses);
    }
    @PutMapping("/update-order/{id}")
    public OrderDtoResponse updateOrder(@PathVariable Long id, @RequestBody OrderDtoRequest orderDtoRequest) {
        return orderService.updateOrder(id, orderDtoRequest);
    }

    @DeleteMapping("/delete-order/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        String message=orderService.deleteOrder(id);
        return ResponseEntity.ok(message);
    }

}
