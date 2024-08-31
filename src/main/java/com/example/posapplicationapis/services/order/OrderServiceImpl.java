package com.example.posapplicationapis.services.order;

import com.example.posapplicationapis.dto.order.OrderDtoRequest;
import com.example.posapplicationapis.dto.order.OrderDtoResponse;
import com.example.posapplicationapis.dto.orderItem.OrderItemDtoResponse;
import com.example.posapplicationapis.entities.*;
import com.example.posapplicationapis.enums.OrderStatus;
import com.example.posapplicationapis.enums.PaymentMethod;
import com.example.posapplicationapis.repositories.*;
import com.example.posapplicationapis.services.orderItem.OrderItemService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderItemsRepository orderItemsRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private OrderItemsRepository orderItemRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderItemService orderItemService;


    @Override
    public OrderDtoResponse createOrder(OrderDtoRequest orderDtoRequest) {
        // Convert DTO to Entity
        Order order = toEntity(orderDtoRequest);
        OrderItem orderItem = new OrderItem();
        Map<Product, Integer> orderedProduct = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : orderDtoRequest.getProductQuantities().entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();
            // Fetch the product from the repository using the product ID
            Product product = productRepository.findById(productId).orElse(null);
            if (product != null) {
                orderedProduct.put(product, quantity);
            } else {
                // Handle the case where the product is not found, if necessary
                // For example, you could log a warning or throw an exception
                System.out.println("Product with ID " + productId + " not found.");
            }
        }
        double  totalPrice = 0.0;
        // Iterate through the map and calculate the total price
        for (Map.Entry<Product, Integer> entry : orderedProduct.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            // Calculate the price for the current product
            double productTotal = product.getPrice() * quantity;
            // Add to the total price
            totalPrice = totalPrice + productTotal;
        }
        orderItem.setPrice(totalPrice);
        orderItem.setOrderedProduct(orderedProduct);
        orderItemsRepository.save(orderItem);
        // Map to store total quantities of each ingredient needed for the order
        Map<Long, Double> totalIngredientQuantities = new HashMap<>();
        // Iterate over each product in the order
        for (Map.Entry<Product, Integer> entry : orderItem.getOrderedProduct().entrySet()) {
            Product product = entry.getKey();
            int orderedProductQuantity = entry.getValue();
            // Retrieve product ingredients and calculate total quantities needed
            for (ProductIngredient productIngredient : product.getIngredients()) {
                for (Map.Entry<Ingredient, Double> ingredientEntry : productIngredient.getIngredientQuantities().entrySet()) {
                    Ingredient ingredient = ingredientEntry.getKey();
                    double ingredientQuantity = ingredientEntry.getValue();
                    // Calculate total quantity needed for this ingredient
                    double totalQuantity = orderedProductQuantity * ingredientQuantity;
                    // Update the map with this ingredient's total quantity
                    totalIngredientQuantities.merge(ingredient.getId(), totalQuantity, Double::sum);
                }
            }
        }
        if (orderDtoRequest.getCustomerId()!= null){
            Customer customer=customerRepository.findById(orderDtoRequest.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("customer not found for customer ID " + orderDtoRequest.getCustomerId()));
            order.setCustomer(customer);
        }

        // Update stock quantities
        for (Map.Entry<Long, Double> ingredientEntry : totalIngredientQuantities.entrySet()) {
            Long ingredientId = ingredientEntry.getKey();
            double requiredQuantity = ingredientEntry.getValue();
            // Fetch the stock for this ingredient
            Stock stock = stockRepository.findByIngredientId(ingredientId)
                    .orElseThrow(() -> new RuntimeException("Stock not found for ingredient ID " + ingredientId));
            // Retrieve the current quantity in stock
            Double currentQuantityInStock = null;
            Ingredient matchingIngredient = null;

            for (Map.Entry<Ingredient, Double> stockEntry : stock.getIngredientStockMap().entrySet()) {
                if (stockEntry.getKey().getId().equals(ingredientId)) {
                    currentQuantityInStock = stockEntry.getValue();
                    matchingIngredient = stockEntry.getKey(); // Save the matching ingredient
                    break;
                }
            }

            // Check if the currentQuantityInStock is null
            if (currentQuantityInStock == null) {
                throw new IllegalArgumentException("Stock quantity for ingredient ID " + ingredientId + " is null");
            }

            // Subtract the required quantity from the stock
            if (currentQuantityInStock - requiredQuantity >= 0){
                double updatedQuantity = currentQuantityInStock - requiredQuantity;
                stock.getIngredientStockMap().put(matchingIngredient, updatedQuantity);
            }else {
                throw new IllegalArgumentException("Stock quantity for ingredient ID " + ingredientId + " is insufficient");
            }

            // Update the stock

            stockRepository.save(stock);
        }
        /*chooseDiscount(order.getId(), customer.getId());*/
         order.setTotal(orderItem.getPrice());
         order.setOrderItem(orderItem);
        // Save the order to the database
        Order savedOrder = orderRepository.save(order);

        // Map the saved order to DTO and return
        return mapToDto(savedOrder);
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
    @Transactional
    public OrderDtoResponse updateOrder(Long id, OrderDtoRequest orderDtoRequest) {
        // Find the existing order by ID
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Update order details from the request DTO
        order.setDate(orderDtoRequest.getDate());
        order.setReceiptNumber(orderDtoRequest.getReceiptNumber());
        order.setStatus(orderDtoRequest.getStatus());

        // Fetch the user and set it in the order
        order.setUser(userRepository.findById(orderDtoRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found")));

        // Fetch the order item
        OrderItem orderItem = orderItemsRepository.findById(order.getOrderItem().getId())
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));

        // Update the product quantities in the order item and adjust the stock
        updateOrderItemAndStock(orderItem, orderDtoRequest.getProductQuantities());

        // Update the order with the updated order item
        order.setOrderItem(orderItem);

        // Recalculate the total price based on the updated order item
        order.setTotal(orderItem.getPrice());

        // Save the updated order
        Order updatedOrder = orderRepository.save(order);

        return mapToDto(updatedOrder);
    }

    private void updateOrderItemAndStock(OrderItem orderItem, Map<Long, Integer> updatedProductQuantities) {
        Map<Product, Integer> currentProductQuantities = orderItem.getOrderedProduct();

        // Calculate the difference between the new and old quantities
        Map<Long, Integer> quantityDifference = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : updatedProductQuantities.entrySet()) {
            Long productId = entry.getKey();
            int newQuantity = entry.getValue();
            int oldQuantity = currentProductQuantities.entrySet().stream()
                    .filter(e -> e.getKey().getId().equals(productId))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .orElse(0);

            quantityDifference.put(productId, newQuantity - oldQuantity);
        }

        // Update stock based on the quantity difference
        adjustStockBasedOnQuantityDifference(quantityDifference);

        // Update the order item with the new product quantities
        for (Map.Entry<Long, Integer> entry : updatedProductQuantities.entrySet()) {
            Long productId = entry.getKey();
            int newQuantity = entry.getValue();
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
            currentProductQuantities.put(product, newQuantity);
        }

        // Recalculate the total price for the order item
        double totalPrice = currentProductQuantities.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
        orderItem.setPrice(totalPrice);
    }

    private void adjustStockBasedOnQuantityDifference(Map<Long, Integer> quantityDifference) {
        for (Map.Entry<Long, Integer> entry : quantityDifference.entrySet()) {
            Long productId = entry.getKey();
            int quantityDiff = entry.getValue();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

            for (ProductIngredient productIngredient : product.getIngredients()) {
                for (Map.Entry<Ingredient, Double> ingredientEntry : productIngredient.getIngredientQuantities().entrySet()) {
                    Ingredient ingredient = ingredientEntry.getKey();
                    double ingredientQuantity = ingredientEntry.getValue();

                    double totalIngredientQuantityChange = quantityDiff * ingredientQuantity;

                    // Adjust the stock for the ingredient
                    Stock stock = stockRepository.findByIngredientId(ingredient.getId())
                            .orElseThrow(() -> new RuntimeException("Stock not found for ingredient ID " + ingredient.getId()));

                    double currentStockQuantity = stock.getIngredientStockMap().getOrDefault(ingredient, 0.0);
                    double updatedStockQuantity = currentStockQuantity - totalIngredientQuantityChange;

                    if (updatedStockQuantity < 0) {
                        throw new IllegalArgumentException("Insufficient stock for ingredient: " + ingredient.getName());
                    }

                    stock.getIngredientStockMap().put(ingredient, updatedStockQuantity);
                    stockRepository.save(stock);
                }
            }
        }
    }








    @Transactional
    @Override
    public OrderDtoResponse removeProductsFromOrder(Long orderId, List<Long> productIds) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderItem orderItem = order.getOrderItem();
        Map<Product, Integer> orderedProducts = orderItem.getOrderedProduct();

        for (Long productId : productIds) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Adjust stock for the removed product
            adjustStockForProductRemoval(product, orderedProducts.get(product));

            // Remove the product from the order
            orderedProducts.remove(product);
        }

        // Update the total price
        double newTotal = orderedProducts.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();
        order.setTotal(newTotal);

        // Save the updated order
        Order updatedOrder = orderRepository.save(order);

        return mapToDto(updatedOrder);
    }
    private void adjustStockForProductRemoval(Product product, int quantityToRemove) {
        for (ProductIngredient productIngredient : product.getIngredients()) {
            for (Map.Entry<Ingredient, Double> ingredientEntry : productIngredient.getIngredientQuantities().entrySet()) {
                Ingredient ingredient = ingredientEntry.getKey();
                double ingredientQuantity = ingredientEntry.getValue();

                double totalIngredientQuantityChange = quantityToRemove * ingredientQuantity;

                Stock stock = stockRepository.findByIngredientId(ingredient.getId())
                        .orElseThrow(() -> new RuntimeException("Stock not found for ingredient ID " + ingredient.getId()));

                double currentStockQuantity = stock.getIngredientStockMap().getOrDefault(ingredient, 0.0);
                double updatedStockQuantity = currentStockQuantity + totalIngredientQuantityChange;

                stock.getIngredientStockMap().put(ingredient, updatedStockQuantity);
                stockRepository.save(stock);
            }
        }
    }


    @Override
    public String deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
        return "order deleted successfully";
    }

    @Override
    public String choosePaymentMethod(Long id, String paymentMethod) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));


        Payment payment = new Payment();
        payment.setPaymentMethod(PaymentMethod.valueOf(paymentMethod.toUpperCase()));
        paymentRepository.save(payment);

        order.setPayment(payment);
        order.setStatus(OrderStatus.POSTED);

        orderRepository.save(order);


        return order.getPayment().getPaymentMethod().toString();
    }

    @Override
    public OrderDtoResponse chooseDiscount(Long customerId,Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        Double total = order.getTotal();
        switch (customer.getRemise()) {
            case FIVE_PERCENT:
                order.setTotal( total- (total*0.05) );
                break;
            case TEN_PERCENT:
                order.setTotal(total - (total*0.10));
                break;
        }
        order.setCustomer(customer);
        Order updateOrder= orderRepository.save(order);
        return mapToDto(updateOrder);
    }









    private OrderDtoResponse mapToDto(Order order) {
        OrderDtoResponse orderDtoResponse = new OrderDtoResponse();
        orderDtoResponse.setId(order.getId());
        orderDtoResponse.setDate(order.getDate());
        OrderItemDtoResponse orderItemDtoResponse = new OrderItemDtoResponse();
        OrderItem orderItem = order.getOrderItem();
/*
        orderItemDtoResponse.setId(orderItem.getId());
*/
        /*orderItemDtoResponse.setPrice(orderItem.getPrice());*/
        orderDtoResponse.setOrderItem(orderItemDtoResponse);
        orderDtoResponse.setReceiptNumber(order.getReceiptNumber());
        orderDtoResponse.setTotal(order.getTotal());
        orderDtoResponse.setStatus(order.getStatus());
        orderDtoResponse.setUserId(order.getUser().getId());
        orderDtoResponse.setCustomerId(order.getCustomer() != null ? order.getCustomer().getId() : null);

        if (order.getOrderItem() != null) {
            OrderItemDtoResponse orderItemDto = new OrderItemDtoResponse();
            orderItemDto.setId(order.getOrderItem().getId());
            orderItemDto.setPrice(order.getOrderItem().getPrice());

            Map<Long, Integer> productIds = order.getOrderItem().getOrderedProduct().entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> entry.getKey().getId(),
                            Map.Entry::getValue
                    ));
            orderItemDto.setProductIds(productIds);
            orderDtoResponse.setOrderItem(orderItemDto);
        }

        return orderDtoResponse;
    }

    public Order toEntity(OrderDtoRequest orderDtoRequest) {
        Order order = new Order();
        order.setDate(orderDtoRequest.getDate());
        order.setReceiptNumber(orderDtoRequest.getReceiptNumber());
        order.setStatus(orderDtoRequest.getStatus());

        User user = userRepository.findById(orderDtoRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        order.setUser(user);



        return order;
    }

}
