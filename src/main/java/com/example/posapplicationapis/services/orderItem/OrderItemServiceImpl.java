package com.example.posapplicationapis.services.orderItem;

import com.example.posapplicationapis.dto.orderItem.OrderItemDtoRequest;
import com.example.posapplicationapis.dto.orderItem.OrderItemDtoResponse;
import com.example.posapplicationapis.entities.OrderItem;
import com.example.posapplicationapis.entities.Product;
import com.example.posapplicationapis.repositories.OrderItemsRepository;
import com.example.posapplicationapis.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class OrderItemServiceImpl implements OrderItemService {
    private OrderItemsRepository orderItemsRepository;
    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    public OrderItemServiceImpl(OrderItemsRepository orderItemsRepository, ProductRepository productRepository) {
        this.orderItemsRepository = orderItemsRepository;
        this.productRepository = productRepository;
        this.modelMapper = new ModelMapper();

    }

    @Override
    public OrderItemDtoResponse createOrderItem(OrderItemDtoRequest requestDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(requestDto.getQuantity());
        orderItem.setPrice(requestDto.getPrice() * requestDto.getQuantity());

        List<Product> products = new ArrayList<>();
        if (requestDto.getProductIds() != null) {
            products = requestDto.getProductIds().stream()
                    .map(productId -> productRepository.findById(productId)
                            .orElseThrow(() -> new RuntimeException("Product not found: " + productId)))
                    .collect(Collectors.toList());
        }

        orderItem.setOrderedProduct(products);

        OrderItem savedOrderItem = orderItemsRepository.save(orderItem);

        // Manually map the savedOrderItem to OrderItemDtoResponse
        OrderItemDtoResponse responseDto = new OrderItemDtoResponse();
        responseDto.setId(savedOrderItem.getId());
        responseDto.setQuantity(savedOrderItem.getQuantity());
        responseDto.setPrice(savedOrderItem.getPrice());

        // Set product IDs in response DTO
        List<Long> productIds = savedOrderItem.getOrderedProduct().stream()
                .map(Product::getId)
                .collect(Collectors.toList());
        responseDto.setProductIds(productIds);

        return responseDto;
    }


    @Override
    public OrderItemDtoResponse getOrderItem(Long id) {
        OrderItem orderItem = orderItemsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));

        // Manually map OrderItem to OrderItemDtoResponse
        OrderItemDtoResponse responseDto = new OrderItemDtoResponse();
        responseDto.setId(orderItem.getId());
        responseDto.setQuantity(orderItem.getQuantity());
        responseDto.setPrice(orderItem.getPrice());

        List<Long> productIds = orderItem.getOrderedProduct().stream()
                .map(Product::getId)
                .collect(Collectors.toList());
        responseDto.setProductIds(productIds);

        return responseDto;
    }

    @Override
    public List<OrderItemDtoResponse> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemsRepository.findAll();
        return toDtoList(orderItems);
    }

    @Transactional
    @Override
    public OrderItemDtoResponse updateOrderItem(Long id, OrderItemDtoRequest request) {
        OrderItem orderItem = orderItemsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));

        orderItem.setQuantity(request.getQuantity());
        orderItem.setPrice(request.getPrice());

        // Fetch products by their IDs
        List<Product> products = request.getProductIds().stream()
                .map(productId -> productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found: " + productId)))
                .collect(Collectors.toList());
        orderItem.setOrderedProduct(products);

        // Save the updated orderItem
        OrderItem updatedOrderItem = orderItemsRepository.save(orderItem);

        // Map the updated orderItem to the response DTO
        return toDto(updatedOrderItem);
    }

    @Override
    public String deleteOrderItem(Long id) {
        OrderItem orderItem = orderItemsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));
        orderItemsRepository.delete(orderItem);
        return "product deleted successfully";
    }


    public static OrderItemDtoResponse toDto(OrderItem orderItem) {
        OrderItemDtoResponse dto = new OrderItemDtoResponse();
        dto.setId(orderItem.getId());

        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        List<Long> productIds = orderItem.getOrderedProduct().stream()
                .map(Product::getId)
                .collect(Collectors.toList());
        dto.setProductIds(productIds);
        return dto;
    }

    public static List<OrderItemDtoResponse> toDtoList(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItemServiceImpl::toDto)
                .collect(Collectors.toList());
    }
}
