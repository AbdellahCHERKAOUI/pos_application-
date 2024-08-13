package com.example.posapplicationapis.services.order;

import com.example.posapplicationapis.dto.order.OrderDtoRequest;
import com.example.posapplicationapis.dto.order.OrderDtoResponse;
import com.example.posapplicationapis.dto.orderItem.OrderItemDtoResponse;
import com.example.posapplicationapis.entities.*;
import com.example.posapplicationapis.enums.OrderStatus;
import com.example.posapplicationapis.enums.PaymentMethod;
import com.example.posapplicationapis.repositories.*;
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


    @Override
    public OrderDtoResponse createOrder(OrderDtoRequest orderDtoRequest) {
        // Convert DTO to Entity
        Order order = toEntity(orderDtoRequest);

        // Fetch OrderItem using the provided ID
        OrderItem orderItem = orderItemRepository.findById(orderDtoRequest.getOrderItemId())
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));

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
    public OrderDtoResponse updateOrder(Long id, OrderDtoRequest orderDtoRequest) {
        // Find the existing order by ID
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Update order details from the request DTO
        order.setDate(orderDtoRequest.getDate());
        order.setReceiptNumber(orderDtoRequest.getReceiptNumber());
        order.setTotal(orderDtoRequest.getTotal());
        order.setStatus(orderDtoRequest.getStatus());

        // Fetch the user and set it in the order
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
    public String chooseDiscount(Long orderId, Long customerId) {
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
        orderRepository.save(order);
        return customer.getRemise().toString();
    }




   /* @Transactional
    @Override
    public OrderItemDtoResponse updateOrderItemInOrder(Long id, OrderItemDtoRequest request) {
        OrderItem orderItem = orderItemsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));

        // Store the old product quantities
        Map<Product, Integer> oldProductQuantities = new HashMap<>(orderItem.getOrderedProduct());

        Map<Product, Integer> newProductQuantities = new HashMap<>();
        double totalPrice = 0.0;

        // Update the ordered products and calculate the total price
        for (Map.Entry<Long, Integer> entry : request.getProductIds().entrySet()) {
            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + entry.getKey()));
            int newQuantity = entry.getValue();
            newProductQuantities.put(product, newQuantity);
            totalPrice += product.getPrice() * newQuantity; // Calculate the total price
        }

        // Adjust the stock levels based on the difference between old and new quantities
        for (Map.Entry<Product, Integer> entry : newProductQuantities.entrySet()) {
            Product product = entry.getKey();
            int newQuantity = entry.getValue();
            int oldQuantity = oldProductQuantities.getOrDefault(product, 0);

            int quantityDifference = newQuantity - oldQuantity;

            if (quantityDifference != 0) {
                // Update stock for the product ingredients
                for (ProductIngredient productIngredient : product.getIngredients()) {
                    for (Map.Entry<Ingredient, Double> ingredientEntry : productIngredient.getIngredientQuantities().entrySet()) {
                        Ingredient ingredient = ingredientEntry.getKey();
                        double ingredientQuantity = ingredientEntry.getValue();

                        // Calculate the quantity change needed for the stock
                        double stockChange = quantityDifference * ingredientQuantity;

                        // Fetch the stock for this ingredient
                        Stock stock = stockRepository.findByIngredientId(ingredient.getId())
                                .orElseThrow(() -> new RuntimeException("Stock not found for ingredient ID " + ingredient.getId()));

                        // Update the stock level
                        Double currentQuantityInStock = stock.getIngredientStockMap().get(ingredient);

                        if (currentQuantityInStock == null) {
                            throw new IllegalArgumentException("Stock quantity for ingredient ID " + ingredient.getId() + " is null");
                        }

                        if (currentQuantityInStock - stockChange < 0) {
                            throw new IllegalArgumentException("Insufficient stock for ingredient ID " + ingredient.getId());
                        }

                        // Update the stock with the new quantity
                        stock.getIngredientStockMap().put(ingredient, currentQuantityInStock - stockChange);
                        stockRepository.save(stock);
                    }
                }
            }
        }

        // Update the order item with new products and price
        orderItem.setOrderedProduct(newProductQuantities);
        orderItem.setPrice(totalPrice);

        // Save the updated orderItem
        OrderItem updatedOrderItem = orderItemsRepository.save(orderItem);

        // Map the updated orderItem to the response DTO
        return toDto(updatedOrderItem);
    }
    @Override
    @Transactional
    public OrderDtoResponse updateOrderByOrderItem(Long id, OrderDtoRequest orderDtoRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setDate(orderDtoRequest.getDate());
        order.setReceiptNumber(orderDtoRequest.getReceiptNumber());
        order.setTotal(orderDtoRequest.getTotal());
        order.setStatus(orderDtoRequest.getStatus());

        // Update user
        order.setUser(userRepository.findById(orderDtoRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found")));

        // Update OrderItem (and adjust stock levels accordingly)
        OrderItemDtoRequest orderItemDtoRequest = new OrderItemDtoRequest();
        orderItemDtoRequest.setProductIds(orderDtoRequest.getOrderItemProductIds()); // Assuming this is provided in the DTO

        OrderItemDtoResponse updatedOrderItem = updateOrderItemInOrder(orderDtoRequest.getOrderItemId(), orderItemDtoRequest);

        // Update the order with the updated OrderItem
        order.setOrderItem(orderItemsRepository.findById(updatedOrderItem.getId())
                .orElseThrow(() -> new RuntimeException("OrderItem not found")));

        // Recalculate the total price based on the updated OrderItem
        order.setTotal(updatedOrderItem.getPrice());

        Order updatedOrder = orderRepository.save(order);

        return mapToDto(updatedOrder);
    }

*/


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
            orderItemDto.setPrice(order.getOrderItem().getPrice());

            // Convert Map<Product, Integer> to Map<Long, Integer> for productIds
            Map<Long, Integer> productIds = order.getOrderItem().getOrderedProduct().entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> entry.getKey().getId(),  // Product ID
                            Map.Entry::getValue               // Quantity
                    ));
            orderItemDto.setProductIds(productIds);
            orderDtoResponse.setOrderItem(orderItemDto);
        }

        return orderDtoResponse;
    }
    public Order toEntity(OrderDtoRequest orderDtoRequest) {
        // Create a new Order entity
        Order order = new Order();

        // Map simple fields
        order.setDate(orderDtoRequest.getDate());
        order.setReceiptNumber(orderDtoRequest.getReceiptNumber());
        order.setStatus(orderDtoRequest.getStatus());

        // Find and set the User entity
        User user = userRepository.findById(orderDtoRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        order.setUser(user);

        // Find and set the OrderItem entity
        OrderItem orderItem = orderItemsRepository.findById(orderDtoRequest.getOrderItemId())
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));
        order.setOrderItem(orderItem);

        // Calculate and set the total order price
        order.setTotal(orderItem.getPrice());

        return order;
    }

}
