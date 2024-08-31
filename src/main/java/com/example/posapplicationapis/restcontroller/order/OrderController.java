package com.example.posapplicationapis.restcontroller.order;

import com.example.posapplicationapis.dto.ProductIdsDto;
import com.example.posapplicationapis.dto.order.OrderDateRequest;
import com.example.posapplicationapis.dto.order.OrderDtoRequest;
import com.example.posapplicationapis.dto.order.OrderDtoResponse;
import com.example.posapplicationapis.dto.order.OrderResponseForReceipt;
import com.example.posapplicationapis.entities.Order;
import com.example.posapplicationapis.services.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @GetMapping("/get-by-user/{userId}")
    public ResponseEntity<List<OrderDtoResponse>> getOrdersByUserId(@PathVariable Long userId) {
         List<OrderDtoResponse> orderDtoResponses = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orderDtoResponses);
    }

    @GetMapping("/get-all")
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

    @PutMapping("/choose-payment-method/{id}")
    public String choosePaymentMethod(@PathVariable Long id, @RequestParam("method") String paymentMethod) {
        return orderService.choosePaymentMethod(id, paymentMethod);
    }

   /* @PutMapping("{orderId}/discount/{customerId}")
    public String chooseDiscount(@PathVariable Long orderId,@PathVariable Long customerId) {
        return orderService.chooseDiscount(orderId, customerId);
    }*/
   /* @PutMapping("/update-order-quantity/{id}")
    public OrderDtoResponse updateOrderQuantity(@PathVariable Long id, @RequestBody OrderDtoRequest orderDtoRequest) {
        return orderService.updateOrderByOrderItem(id, orderDtoRequest);
    }*/
   @PutMapping(value = "/{id}/remove-products")
   public ResponseEntity<OrderDtoResponse> removeProductsFromOrder(
           @PathVariable Long id,
           @RequestBody ProductIdsDto productIds) {
       OrderDtoResponse updatedOrder = orderService.removeProductsFromOrder(id, productIds.getProductIds());
       return ResponseEntity.ok(updatedOrder);
   }

    @PutMapping("/update-status/{orderId}")
    public String updateOrderStatus(@RequestParam String orderStatus, @PathVariable Long orderId) {
        return orderService.updateOrderStatus(orderId, orderStatus);
    }

    @PutMapping("/pay/{orderId}")
    public String pay(@PathVariable Long orderId) {
        return orderService.setPaid(orderId);
    }

    @GetMapping("/get-products/{orderId}")
    public OrderResponseForReceipt getProducts(@PathVariable Long orderId) {
        return orderService.getProducts(orderId);
    }

    @PostMapping("/get-by-day")
    public List<OrderDtoResponse> getOrdersByDay(@RequestBody OrderDateRequest orderDateRequest) {
        LocalDate date = orderDateRequest.getDate().toLocalDate();
        return orderService.getOrdersByDay(date);
    }

    @PutMapping(value = "/{customerId}/{orderId}")
    public ResponseEntity<OrderDtoResponse> choosediscount(
            @PathVariable Long customerId,
            @PathVariable Long orderId) {
        OrderDtoResponse updatedOrder = orderService.chooseDiscount(customerId,orderId);
        return ResponseEntity.ok(updatedOrder);
    }
}
