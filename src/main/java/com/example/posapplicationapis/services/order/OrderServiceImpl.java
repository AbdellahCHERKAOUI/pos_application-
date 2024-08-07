package com.example.posapplicationapis.services.order;

import com.example.posapplicationapis.dto.order.OrderDtoRequest;
import com.example.posapplicationapis.dto.order.OrderDtoResponse;
import com.example.posapplicationapis.dto.orderItem.OrderItemDtoResponse;
import com.example.posapplicationapis.entities.Order;
import com.example.posapplicationapis.entities.OrderItem;
import com.example.posapplicationapis.repositories.OrderItemsRepository;
import com.example.posapplicationapis.repositories.OrderRepository;
import com.example.posapplicationapis.repositories.PaymentRepository;
import com.example.posapplicationapis.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    private UserRepository userRepository;
    private PaymentRepository paymentRepository;
    private OrderItemsRepository orderItemsRepository;
    private ModelMapper modelMapper;
    private OrderRepository orderRepository;

    public OrderServiceImpl(UserRepository userRepository, PaymentRepository paymentRepository, OrderItemsRepository orderItemsRepository,
                            ModelMapper modelMapper,OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.modelMapper = modelMapper;
        this.orderRepository=orderRepository;
    }


   @Override
   public OrderDtoResponse createOrder(OrderDtoRequest orderDtoRequest) {
       Order order = new Order();
       order.setDate(orderDtoRequest.getDate());
       order.setReceiptNumber(orderDtoRequest.getReceiptNumber());
       order.setStatus(orderDtoRequest.getStatus());

       order.setUser(userRepository.findById(orderDtoRequest.getUserId())
               .orElseThrow(() -> new RuntimeException("User not found")));

       OrderItem orderItem = orderItemsRepository.findById(orderDtoRequest.getOrderItemId())
               .orElseThrow(() -> new RuntimeException("OrderItem not found"));

       // Set order item and calculate total
       order.setOrderItem(orderItem);
       double total = orderItem.getPrice() * orderItem.getQuantity();
       order.setTotal(total);

       Order savedOrder = orderRepository.save(order);

       return mapToDto(savedOrder);
   }

    private OrderDtoResponse mapToDto(Order order) {
        OrderDtoResponse orderDtoResponse = new OrderDtoResponse();
        orderDtoResponse.setId(order.getId());
        orderDtoResponse.setDate(order.getDate());
        orderDtoResponse.setReceiptNumber(order.getReceiptNumber());
        orderDtoResponse.setTotal(order.getTotal());
        orderDtoResponse.setStatus(order.getStatus());
        orderDtoResponse.setUserId(order.getUser().getId());
        orderDtoResponse.setCustomerId(order.getCustomer() != null ? order.getCustomer().getId() : null);

        if (order.getOrderItem() != null) {
            OrderItemDtoResponse orderItemDto = new OrderItemDtoResponse();
            orderItemDto.setId(order.getOrderItem().getId());
            orderItemDto.setQuantity(order.getOrderItem().getQuantity());
            orderItemDto.setPrice(order.getOrderItem().getPrice());
            orderItemDto.setProductIds(order.getOrderItem().getOrderedProduct().stream()
                    .map(product -> product.getId())
                    .collect(Collectors.toList()));
            orderDtoResponse.setOrderItem(orderItemDto);
        }

        return orderDtoResponse;
    }
    @Override
    public OrderDtoResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return mapToDto(order);
    }

    @Override
    public List<OrderDtoResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDtoResponse updateOrder(Long id, OrderDtoRequest orderDtoRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setDate(orderDtoRequest.getDate());
        order.setReceiptNumber(orderDtoRequest.getReceiptNumber());
        order.setTotal(orderDtoRequest.getTotal());
        order.setStatus(orderDtoRequest.getStatus());

        order.setUser(userRepository.findById(orderDtoRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found")));

        order.setOrderItem(orderItemsRepository.findById(orderDtoRequest.getOrderItemId())
                .orElseThrow(() -> new RuntimeException("OrderItem not found")));

        Order updatedOrder = orderRepository.save(order);

        return mapToDto(updatedOrder);
    }

    @Override
    public String deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
        return "order deleted successfully";
    }
}
